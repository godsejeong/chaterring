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
import com.cclss.chattering.data.MailData
import com.cclss.chattering.util.Utils.memberSearch
import com.cclss.chattering.view.MailDetailActivity
import com.cclss.chattering.view.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.realm.Realm
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val BROADCAST_MESSAGE = "com.cclss.chattering.fcm"

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.e("Firebase", "FirebaseInstanceIDService : $s")
    }

    init {
        Realm.init(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var title = remoteMessage.data.get("title")
        var body = remoteMessage.data.get("body")
        var image = remoteMessage.data.get("image")
        var memberType = remoteMessage.data.get("memberType")
        var time = getRealTime()
        var id = createID()
        sendNotification(title, body, image, memberType, time, id)
        saveRealm(title, body, image, memberType, time, id)
    }

    private fun saveRealm(
        title: String?,
        content: String?,
        img: String?,
        memberType: String?,
        time: String?,
        id: Int?
    ) {
        var realm = Realm.getDefaultInstance()

        realm.executeTransactionAsync { realm ->
            var maildata = realm.createObject(MailData::class.java)
            maildata.apply {
                this.id = id!!
                this.title = title!!
                this.content = content!!
                this.img = img!!
                this.memberType = memberType!!
                this.time = time!!
            }
            realm.copyToRealm(maildata)
            realm.commitTransaction()
        }

        val intent = Intent(BROADCAST_MESSAGE)
        intent.putExtra("title", title)
        intent.putExtra("body", content)
        intent.putExtra("image", img)
        intent.putExtra("memberType", memberType)
        intent.putExtra("time", time)
        intent.putExtra("id", id)
        sendBroadcast(intent)
    }

    private fun sendNotification(
        title: String?,
        messageBody: String?,
        image: String?,
        memberType: String?,
        time: String?,
        id: Int?
    ) {
        val allTime = time!!.substring(0, time.lastIndexOf("/"))

        val backIntent = Intent(this, MainActivity::class.java)
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val intent = Intent(this, MailDetailActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("content", messageBody)
        intent.putExtra("img", image)
        intent.putExtra("profile", memberSearch(memberType))
        intent.putExtra("name", memberType)
        intent.putExtra("time", allTime)
        intent.putExtra("id", id)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val id: Int = createID()
        val pendingIntent = PendingIntent.getActivities(
            this,
            id,
            arrayOf(backIntent, intent),
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var bitmapImage = image?.run { getBitmapFromURL(image) }
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
                    if(image != "") {
                        setLargeIcon(bitmapImage)
                        setStyle(bigPictureStyle)
                    }
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

    fun createID(): Int {
        val dateTime = DateTime()
        val formatter: DateTimeFormatter = DateTimeFormat.forPattern("MMddHHmmss")
        return formatter.print(dateTime).toInt()
    }

    fun getRealTime(): String {
        val dateTime = DateTime()
        var allTime = dateTime.toString("yyyy년 MM월 dd일 (E) a HH:mm/MM.dd", Locale.KOREA)
        return allTime
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
