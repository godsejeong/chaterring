package com.cclss.chattering.data.source.mail

import androidx.lifecycle.MutableLiveData
import com.cclss.chattering.util.Utils
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemMail
import com.cclss.chattering.data.MailData
import io.realm.Realm
import io.realm.RealmResults

class MailDataSource : MailSource {
    private val mailItem = MutableLiveData<ArrayList<ItemDataInterface>>()

    override fun getMails(realm : Realm,loadMailCallback: MailSource.LoadMailCallback) {
        mailItem.value = arrayListOf()
        val allData = realm.where(MailData::class.java).findAll()

        allData.forEach {
            var profile = Utils.memberSearch(it.memberType)
            mailItem.value?.add(ItemMail(it.id,profile, it.memberType, it.title, it.content, it.img,it.time,it.isCheck))
        }



        loadMailCallback.onLoadMails(mailItem.value!!)
    }

    override fun deleteMail(realm: Realm, deleteMailCallback: MailSource.DeleteMailCallback) {
        realm.executeTransaction { realm ->
            val result: RealmResults<MailData> =
                realm.where(MailData::class.java)
                    .findAll()
            result.deleteAllFromRealm()
        }

        mailItem.value?.clear()
        deleteMailCallback.onDeleteMail(mailItem.value!!)
    }

    override fun updateMail(itemMail: ItemMail,updateMailCallback: MailSource.UpdateMailCallback) {
        mailItem.value!!.add(itemMail)
        updateMailCallback.onUpdateMail(mailItem.value!!)
    }


}