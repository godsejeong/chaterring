package com.cclss.chattering.data.source.mail

import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemMail
import io.realm.Realm

interface MailSource {

    interface LoadMailCallback{
        fun onLoadMails(item: ArrayList<ItemDataInterface>)
    }

    interface DeleteMailCallback{
        fun onDeleteMail(item : ArrayList<ItemDataInterface>)
    }

    interface UpdateMailCallback{
        fun onUpdateMail(item : ArrayList<ItemDataInterface>)
    }

    fun getMails(realm : Realm,loadMailCallback: LoadMailCallback)
    fun deleteMail(realm: Realm,deleteMailCallback: DeleteMailCallback)
    fun updateMail(itemMail : ItemMail,updateMailCallback: UpdateMailCallback)
}