package com.cclss.chattering.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cclss.chattering.R
import com.cclss.chattering.data.MailData
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_mail_detail.*


class MailDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_detail)
        Realm.init(this)

        var id = intent.getIntExtra("id",0)

        Log.e("id", id.toString())

        val realm = Realm.getDefaultInstance()

        realm.executeTransaction { realm ->
            val mailData: MailData? = realm.where(MailData::class.java).equalTo("id",id).findFirst()
            mailData!!.isCheck = true
        }

        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")
        var img = intent.getStringExtra("img")
        var profile = intent.getStringExtra("profile")
        var name = intent.getStringExtra("name")
        var time = intent.getStringExtra("time")

        mailTimeTv.text = time

        Glide.with(this)
            .load(profile)
            .override(150, 150)
            .into(mailProfileImg)

        Glide.with(this)
            .load(img)
            .into(mainContentImg)

        mailProfileName.text = name
        mailTitleTv.text = title
        mailContentTv.text = content
    }
}
