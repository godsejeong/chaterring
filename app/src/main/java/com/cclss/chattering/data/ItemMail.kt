package com.cclss.chattering.data

import com.cclss.chattering.data.ItemDataState.MAIL

data class ItemMail(val id : Int,val profile : String,val name : String,val title : String,val content : String,val img : String?,val time : String?,val isCheck : Boolean) : ItemDataInterface {
    override fun getItem(): Int {
        return MAIL
    }
}