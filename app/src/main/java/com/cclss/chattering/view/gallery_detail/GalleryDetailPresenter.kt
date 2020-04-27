package com.cclss.chattering.view.gallery_detail

import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.adapter.ItemListAdapter
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.source.gallery.GalleryDataRepository
import com.cclss.chattering.data.source.gallery.GallerySource
import io.realm.Realm

class GalleryDetailPresenter : GalleryDetailContract.Presenter{

    override lateinit var view: GalleryDetailContract.View
    override lateinit var galleryDetilRecyclerView: RecyclerView
    override lateinit var galleryDataRepository: GalleryDataRepository

    override fun loadGallery(realm : Realm,member : String) {
        galleryDataRepository.loadGalleryDetail(realm,member,object : GallerySource.LoadImageDetailCallback{
            override fun onLoadImageDetails(items: ArrayList<ItemDataInterface>) {
                updateRecylcerView(galleryDetilRecyclerView,items)
            }
        })
    }

    fun updateRecylcerView(ry : RecyclerView,items : ArrayList<ItemDataInterface>){
        ItemListAdapter().apply {
            ry.adapter = this
            this.submitList(items)
        }
    }


}