package com.cclss.chattering.data

import com.cclss.chattering.data.ItemDataState.MAIL

data class ItemMail(val profile : String,val name : String,val title : String,val content : String,val img : String?) : ItemDataInterface {
    override fun getItem(): Int {
        return MAIL
    }
}