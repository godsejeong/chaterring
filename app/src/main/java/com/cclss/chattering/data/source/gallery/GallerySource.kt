package com.cclss.chattering.data.source.gallery

import com.cclss.chattering.data.ItemDataInterface
import io.realm.Realm

interface GallerySource{

    interface LoadImageCallback{
        fun onLoadImages(items : ArrayList<ItemDataInterface>)
    }

    interface LoadImageDetailCallback{
        fun onLoadImageDetails(items : ArrayList<ItemDataInterface>)
    }

    fun loadGallery(realm: Realm,loadImageCallback: LoadImageCallback)
    fun loadGalleryDetail(realm: Realm,member : String,loadImageDetailCallback: LoadImageDetailCallback)
}