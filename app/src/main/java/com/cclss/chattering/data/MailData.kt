package com.cclss.chattering.data

import com.google.gson.JsonObject
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

open class MailData() : RealmObject(){
    @Required
    var title : String = ""
    var content : String = ""
    var img : String = ""
    var memberType : String = ""
    var time : String = ""
    var id : Int = 0
    var isCheck : Boolean = false
}