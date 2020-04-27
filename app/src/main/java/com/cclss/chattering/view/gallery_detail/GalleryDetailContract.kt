package com.cclss.chattering.view.gallery_detail

import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.data.source.gallery.GalleryDataRepository
import io.realm.Realm

interface GalleryDetailContract{
    interface View{
        fun toastMessage(text : String)
    }

    interface Presenter{
        var view : View
        var galleryDetilRecyclerView : RecyclerView
        var galleryDataRepository : GalleryDataRepository

        fun loadGallery(realm : Realm,member : String)
    }
}