package com.cclss.chattering.view.main

import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.adapter.ItemListAdapter
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemMail
import com.cclss.chattering.data.source.mail.MailDataRepository
import io.realm.Realm

interface MainContract {
    interface View{
        fun toastMessage(text : String)
    }

    interface Presenter{
        var view : View
        var listAdapter : ItemListAdapter
        var mailRecyclerView : RecyclerView
        var memberRecyclerView : RecyclerView
        var mailData : MailDataRepository

        fun loadMember(item : ArrayList<ItemDataInterface>)
        fun loadMail(realm: Realm)
        fun updateMail(itemMail : ItemMail)
        fun deleteMail(realm: Realm)
    }
}