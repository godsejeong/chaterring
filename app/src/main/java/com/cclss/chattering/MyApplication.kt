package com.cclss.chattering

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(applicationContext)
        var config = RealmConfiguration.Builder().name("mail.realm").build()
        Realm.setDefaultConfiguration(config)
    }
}