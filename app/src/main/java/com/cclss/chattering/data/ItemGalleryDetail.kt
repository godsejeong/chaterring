package com.cclss.chattering.data

import com.cclss.chattering.data.ItemDataState.GALLERY_DETAIL

data class ItemGalleryDetail(val img : String) : ItemDataInterface{
    override fun getItem(): Int {
        return GALLERY_DETAIL
    }
}