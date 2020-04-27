package com.cclss.chattering.data

import com.cclss.chattering.data.ItemDataState.GALLERY

data class ItemGallery(val title : String, val index : String, val image :String) : ItemDataInterface{
    override fun getItem(): Int {
        return GALLERY
    }

}