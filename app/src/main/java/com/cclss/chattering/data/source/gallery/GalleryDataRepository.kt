package com.cclss.chattering.data.source.gallery

import com.cclss.chattering.data.ItemDataInterface
import io.realm.Realm

class GalleryDataRepository : GallerySource{
    private val calendarDataSource = GalleryDataSource()
    override fun loadGallery(realm: Realm, loadImageCallback: GallerySource.LoadImageCallback) {
        calendarDataSource.loadGallery(realm, object : GallerySource.LoadImageCallback {
            override fun onLoadImages(items: ArrayList<ItemDataInterface>) {
                loadImageCallback.onLoadImages(items)
            }
        })
    }

    override fun loadGalleryDetail(
        realm: Realm,
        member: String,
        loadImageDetailCallback: GallerySource.LoadImageDetailCallback
    ) {
        calendarDataSource.loadGalleryDetail(realm,member,object :GallerySource.LoadImageDetailCallback{
            override fun onLoadImageDetails(items: ArrayList<ItemDataInterface>) {
                loadImageDetailCallback.onLoadImageDetails(items)
            }
        })
    }
}