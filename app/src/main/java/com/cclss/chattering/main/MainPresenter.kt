package com.cclss.chattering.main

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.MailDetailActivity
import com.cclss.chattering.Utils
import com.cclss.chattering.adapter.ItemListAdapter
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemMail
import com.cclss.chattering.data.ItemMember
import com.cclss.chattering.data.MailData
import com.cclss.chattering.data.source.mail.MailDataRepository
import com.cclss.chattering.data.source.mail.MailSource
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainPresenter() : MainContract.Presenter {
    override lateinit var view: MainContract.View
    override lateinit var listAdapter: ItemListAdapter
    override lateinit var mailRecyclerView: RecyclerView
    override lateinit var memberRecyclerView: RecyclerView
    override lateinit var mailData: MailDataRepository

    override fun loadMember(item : ArrayList<ItemDataInterface>) {
        recyclerViewSet(memberRecyclerView,item)
    }

    override fun loadMail(realm: Realm) {
        mailData.getMails(realm,object : MailSource.LoadMailCallback{
            override fun onLoadMails(item: ArrayList<ItemDataInterface>) {
                recyclerViewSet(mailRecyclerView,item)
            }
        })
    }

    override fun updateMail(itemMail: ItemMail) {
        mailData.updateMail(itemMail,object : MailSource.UpdateMailCallback{
            override fun onUpdateMail(item: ArrayList<ItemDataInterface>) {
                recyclerViewSet(mailRecyclerView,item)
            }
        })
    }

    override fun deleteMail(realm: Realm) {
        mailData.deleteMail(realm,object : MailSource.DeleteMailCallback{
            override fun onDeleteMail(item: ArrayList<ItemDataInterface>) {
                recyclerViewSet(mailRecyclerView,item)
            }
        })
    }

    private fun recyclerViewSet(ry: RecyclerView, items: ArrayList<ItemDataInterface>) {
        ItemListAdapter().apply {
            ry.adapter = this
            this.submitList(items)

            setOnItemClickListener(object : ItemListAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    var item = items[position] as ItemMail
                    var intent = Intent(
                        view.context,
                        MailDetailActivity::class.java
                    )
                    intent.putExtra("title", item.title)
                    intent.putExtra("content", item.content)
                    intent.putExtra("img", item.img)
                    intent.putExtra("profile", Utils.memberSearch(item.name))
                    intent.putExtra("name", item.name)
                    view.context.startActivity(intent)
                }
            })
        }
    }
}