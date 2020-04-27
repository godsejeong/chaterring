package com.cclss.chattering.view.gallery

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cclss.chattering.R
import com.cclss.chattering.data.source.gallery.GalleryDataRepository
import com.cclss.chattering.util.RecyclerViewDecoration
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_gallery.*


class GalleryActivity : AppCompatActivity() , GalleryContract.View{
    lateinit var realm: Realm
    lateinit var presenter : GalleryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        Realm.init(this)
        realm = Realm.getDefaultInstance()

        galleryBackBtn.setOnClickListener { finish() }

        val itemDecoration =
            RecyclerViewDecoration(this,R.dimen.gallery_padding_width,R.dimen.gallery_padding_height)
        galleryRy.addItemDecoration(itemDecoration)

        presenter = GalleryPresenter().apply {
            view = this@GalleryActivity
            galleryRecyclerView = galleryRy
            galleryDataRepository = GalleryDataRepository()
        }

        presenter.loadGallery(realm)
    }

    override fun toastMessage(text: String) {
        Toast.makeText(this@GalleryActivity,text, Toast.LENGTH_SHORT).show() }
}
