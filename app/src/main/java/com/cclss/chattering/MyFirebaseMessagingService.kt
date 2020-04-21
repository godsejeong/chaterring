package com.cclss.chattering

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cclss.chattering.Utils.memberSearch
import com.cclss.chattering.data.MailData
import com.cclss.chattering.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.realm.Realm
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val BROADCAST_MESSAGE = "com.cclss.chattering.fcm"

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.e("Firebase", "FirebaseInstanceIDService : $s")
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var title = remoteMessage.data.get("title")
        var body = remoteMessage.data.get("body")
        var image = remoteMessage.data.get("image")
        var memberType = remoteMessage.data.get("memberType")
        sendNotification(title, body, image, memberType)

        saveRealm(title, body, image, memberType)

        val intent = Intent(BROADCAST_MESSAGE)
        intent.putExtra("title", title)
        intent.putExtra("body", body)
        intent.putExtra("image", image)
        intent.putExtra("memberType", memberType)
        sendBroadcast(intent)
    }

    private fun saveRealm(title: String?, content: String?, img: String?, memberType: String?) {
        var realm = Realm.getDefaultInstance()

        realm.executeTransactionAsync { realm ->
            var mail = realm.createObject(MailData::class.java)

            mail.apply {
                this.title = title!!
                this.content = content!!
                this.img = img!!
                this.memberType = memberType!!
            }
            realm.copyToRealm(mail)
            realm.commitTransaction()
        }

    }

    private fun sendNotification(
        title: String?,
        messageBody: String?,
        image: String?,
        memberType: String?
    ) {
        val intent = Intent(this, MailDetailActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("content", messageBody)
        intent.putExtra("img", image)
        intent.putExtra("profile", memberSearch(memberType))
        intent.putExtra("name", memberType)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var bitmapImage = image?.run { getBitmapFromURL(image!!) }
        val bigPictureStyle =
            NotificationCompat.BigPictureStyle()
        bigPictureStyle.bigPicture(bitmapImage)
        val notification =
            NotificationCompat.Builder(applicationContext, "channel").apply {
                setSmallIcon(R.drawable.ic_launcher_background)
                setContentTitle(title)
                setContentText(messageBody)
                setAutoCancel(true)
                image?.run {
                    setLargeIcon(bitmapImage)
                    setStyle(bigPictureStyle)
                }
                setSound(defaultSoundUri)
                setContentIntent(pendingIntent)
            }.build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("channel", "channel", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "channel Notify"
            channel.setShowBadge(false)

            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(20010906, notification)
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
