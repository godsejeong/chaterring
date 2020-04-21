package com.cclss.chattering.data

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class MailData() : RealmObject(){
    @Required
    lateinit var title : String
    lateinit var content : String
    lateinit var img : String
    lateinit var memberType : String
}