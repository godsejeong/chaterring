package com.cclss.chattering.view.gallery

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.adapter.ItemListAdapter
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemGallery
import com.cclss.chattering.data.source.gallery.GalleryDataRepository
import com.cclss.chattering.data.source.gallery.GallerySource
import com.cclss.chattering.view.gallery_detail.GalleryDetailActivity
import io.realm.Realm

class GalleryPresenter : GalleryContract.Presenter{

    override lateinit var view: GalleryContract.View
    override lateinit var galleryRecyclerView: RecyclerView
    override lateinit var galleryDataRepository: GalleryDataRepository

    override fun loadGallery(realm : Realm) {
        galleryDataRepository.loadGallery(realm,object : GallerySource.LoadImageCallback{
            override fun onLoadImages(items: ArrayList<ItemDataInterface>) {
                updateRecylcerView(galleryRecyclerView,items)
            }
        })
    }



    fun updateRecylcerView(ry : RecyclerView,items : ArrayList<ItemDataInterface>){
        ItemListAdapter().apply {
            ry.adapter = this
            this.submitList(items)

            setOnItemClickListener(object : ItemListAdapter.ItemClick {
                override fun onClick(view: View, position: Int, viewType: Int) {
                    var item = items[position] as ItemGallery
                    var intent = Intent(view.context,GalleryDetailActivity::class.java)
                    intent.putExtra("member",item.title)
                    view.context.startActivity(intent)
                }
            })
        }
    }


}