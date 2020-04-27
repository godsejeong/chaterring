package com.cclss.chattering.view.main

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.R
import com.cclss.chattering.util.Utils
import com.cclss.chattering.adapter.ItemListAdapter
import com.cclss.chattering.data.BaseViewHolder
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemDataState.MAIL
import com.cclss.chattering.data.ItemDataState.MEMBER
import com.cclss.chattering.data.ItemMail
import com.cclss.chattering.data.ItemMember
import com.cclss.chattering.data.source.mail.MailDataRepository
import com.cclss.chattering.data.source.mail.MailSource
import com.cclss.chattering.view.MailDetailActivity
import com.google.gson.Gson
import io.realm.Realm

class MainPresenter : MainContract.Presenter {
    override lateinit var view: MainContract.View
    override lateinit var listAdapter: ItemListAdapter
    override lateinit var mailRecyclerView: RecyclerView
    override lateinit var memberRecyclerView: RecyclerView
    override lateinit var mailData: MailDataRepository
    override lateinit var realm: Realm

    var seleteMember = ""

    fun onResume(){
        loadMail(seleteMember)
    }

    override fun loadMember(item: ArrayList<ItemDataInterface>) {
        recyclerViewSet(memberRecyclerView, item)
    }

    override fun loadMail(member : String) {
        seleteMember = member
        mailData.getMails(realm,member,object : MailSource.LoadMailCallback {
            override fun onLoadMails(item: ArrayList<ItemDataInterface>) {
                recyclerViewSet(mailRecyclerView,item)
                if(item.size <= 0)
                    this@MainPresenter.view.toastMessage("수신받은 메일이 존재하지 않습니다.")
            }
        })
    }

    override fun updateMail(itemMail: ItemMail) {
        mailData.updateMail(itemMail, object : MailSource.UpdateMailCallback {
            override fun onUpdateMail(item: ArrayList<ItemDataInterface>) {
                recyclerViewSet(mailRecyclerView, item)
            }
        })
    }

    override fun deleteMail() {
        mailData.deleteMail(realm, object : MailSource.DeleteMailCallback {
            override fun onDeleteMail(item: ArrayList<ItemDataInterface>) {
                recyclerViewSet(mailRecyclerView, item)
            }
        })
    }

    private fun recyclerViewSet(ry: RecyclerView, items: ArrayList<ItemDataInterface>) {
        ItemListAdapter().apply {
            ry.adapter = this
            this.submitList(items)

            setOnItemClickListener(object : ItemListAdapter.ItemClick {
                override fun onClick(view: View, position: Int, viewType: Int) {
                    when (viewType) {
                        MAIL -> {
                            this@MainPresenter.view.toastMessage("position:$position")
                            var item = items[position] as ItemMail
                            var intent = Intent(
                                view.context,
                                MailDetailActivity::class.java
                            )
                            val allTime = item.time!!.substring(0, item.time!!.lastIndexOf("/"))

                            intent.putExtra("title", item.title)
                            intent.putExtra("content", item.content)
                            intent.putExtra("img", item.img)
                            intent.putExtra("profile", Utils.memberSearch(item.name))
                            intent.putExtra("name", item.name)
                            intent.putExtra("id", item.id)
                            intent.putExtra("time", allTime)
                            view.context.startActivity(intent)
                        }

                        MEMBER -> {
                            var item = items[position] as ItemMember
                            loadMail(item.name)
                        }
                    }
                }
            })
        }
    }
}