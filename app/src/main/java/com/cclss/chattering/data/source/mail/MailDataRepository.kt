package com.cclss.chattering.data.source.mail

import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemMail
import io.realm.Realm

class MailDataRepository () : MailSource{

    private val mailDataSource = MailDataSource()

    override fun getMails(realm: Realm, loadMailCallback: MailSource.LoadMailCallback) {
        mailDataSource.getMails(realm,object : MailSource.LoadMailCallback{
            override fun onLoadMails(item: ArrayList<ItemDataInterface>) {
                loadMailCallback.onLoadMails(item)
            }
        })
    }

    override fun deleteMail(realm: Realm, deleteMailCallback: MailSource.DeleteMailCallback) {
        mailDataSource.deleteMail(realm,object : MailSource.DeleteMailCallback{
            override fun onDeleteMail(item: ArrayList<ItemDataInterface>) {
                deleteMailCallback.onDeleteMail(item)
            }
        })
    }

    override fun updateMail(itemMail: ItemMail, updateMailCallback: MailSource.UpdateMailCallback) {
        mailDataSource.updateMail(itemMail,object : MailSource.UpdateMailCallback{
            override fun onUpdateMail(item: ArrayList<ItemDataInterface>) {
                updateMailCallback.onUpdateMail(item)
            }
        })
    }
}