package com.cclss.chattering.data

data class ItemMember(val name : String, val img : String, val isSubscription : Boolean = false) : ItemDataInterface{
    override fun getItem(): Int {
        return ItemDataState.MEMBER
    }
}
