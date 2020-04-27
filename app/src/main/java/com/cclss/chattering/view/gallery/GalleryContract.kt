package com.cclss.chattering.view.gallery

import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.data.source.gallery.GalleryDataRepository
import io.realm.Realm

interface GalleryContract{
    interface View{
        fun toastMessage(text : String)
    }

    interface Presenter{
        var view : View
        var galleryRecyclerView : RecyclerView
        var galleryDataRepository : GalleryDataRepository

        fun loadGallery(realm : Realm)
    }
}