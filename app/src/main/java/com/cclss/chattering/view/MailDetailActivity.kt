package com.cclss.chattering.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cclss.chattering.R
import kotlinx.android.synthetic.main.activity_mail_detail.*

class MailDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_detail)

        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")
        var img = intent.getStringExtra("img")
        var profile = intent.getStringExtra("profile")
        var name = intent.getStringExtra("name")

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
