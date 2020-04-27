package com.cclss.chattering.view.gallery_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cclss.chattering.R
import com.cclss.chattering.data.source.gallery.GalleryDataRepository
import com.cclss.chattering.util.RecyclerViewDecoration
import com.cclss.chattering.view.gallery.GalleryPresenter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_gallery_detail.*

class GalleryDetailActivity : AppCompatActivity(), GalleryDetailContract.View {
    lateinit var realm: Realm
    lateinit var presenter : GalleryDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_detail)

        var member = intent.getStringExtra("member")

        Realm.init(this)
        realm = Realm.getDefaultInstance()

        galleryDetailTv.text = member
        galleryDetailBackBtn.setOnClickListener {
            finish()
        }

        val itemDecoration =
            RecyclerViewDecoration(this,R.dimen.gallery_detail_padding_width,R.dimen.gallery_detail_padding_height)
        galleryDetailRy.addItemDecoration(itemDecoration)

        presenter = GalleryDetailPresenter().apply {
            view = this@GalleryDetailActivity
            galleryDetilRecyclerView = galleryDetailRy
            galleryDataRepository = GalleryDataRepository()
        }

        presenter.loadGallery(realm,member)
    }


    override fun toastMessage(text: String) {
        Toast.makeText(this@GalleryDetailActivity, text, Toast.LENGTH_SHORT).show()
    }

}
