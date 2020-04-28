package com.cclss.chattering.data.source.gallery

import androidx.lifecycle.MutableLiveData
import com.cclss.chattering.data.ItemGallery
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemGalleryDetail
import com.cclss.chattering.data.MailData
import io.realm.Realm

class GalleryDataSource : GallerySource {
    private val galleryItem = MutableLiveData<ArrayList<ItemDataInterface>>()
    private val galleryDetailItem = MutableLiveData<ArrayList<ItemDataInterface>>()
    private var memberType = ""
    private var checkArray = ArrayList<String>()
    override fun loadGallery(realm: Realm, loadImageCallback: GallerySource.LoadImageCallback) {
        galleryItem.value = arrayListOf()
        realm.executeTransactionAsync { realm ->

            val allData = realm.where(MailData::class.java).findAll()

            allData.forEach loop@{
                if(memberType != it.memberType) {
                    checkArray.forEach {text ->
                        if(it.memberType == text){
                            return@loop
                        }
                    }

                    checkArray.add(it.memberType)
                    memberType = it.memberType
                    var memberData =
                        realm.where(MailData::class.java).equalTo("memberType",memberType).findAll()
                    galleryItem.value!!.add(ItemGallery(memberType,memberData.size.toString(),it.img))
                }
            }
        }

        loadImageCallback.onLoadImages(galleryItem.value!!)
    }

    override fun loadGalleryDetail(
        realm: Realm,
        member: String,
        loadImageDetailCallback: GallerySource.LoadImageDetailCallback
    ) {
        galleryDetailItem.value = arrayListOf()

        realm.executeTransactionAsync { realm ->
            var memberData =
                realm.where(MailData::class.java).equalTo("memberType",member).findAll()

            memberData.forEach {
                if(it.img != "")
                    galleryDetailItem.value!!.add(ItemGalleryDetail(it.img))
            }
        }

        loadImageDetailCallback.onLoadImageDetails(galleryDetailItem.value!!)
    }

}