package com.cclss.chattering.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.MailDetailActivity
import com.cclss.chattering.R
import com.cclss.chattering.Utils.memberSearch
import com.cclss.chattering.adapter.ItemListAdapter
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemMail
import com.cclss.chattering.data.ItemMember
import com.cclss.chattering.data.MailData
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainView {
    lateinit var realm: Realm
    private val BROADCAST_MESSAGE = "com.cclss.chattering.fcm"
    private var mReceiver: BroadcastReceiver? = null

    private val mailItem_ = MutableLiveData<ArrayList<ItemDataInterface>>()

    var memberItem = ArrayList<ItemDataInterface>().apply {
        add(
            ItemMember(
                "장원영",
                "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-jang-wonyoung.jpg",
                true
            )
        )
        add(
            ItemMember(
                "사쿠라",
                "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-miyawaki-sakura.jpg",
                true
            )
        )
        add(
            ItemMember(
                "조유리",
                "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-jo-yuri.jpg",
                true
            )
        )
        add(ItemMember("최예나", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-choi-yena.jpg"))
        add(ItemMember("안유진", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-an-yujin.jpg"))
        add(ItemMember("나코", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-yabuki-nako.jpg"))
        add(ItemMember("권은비", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kwon-eunbi.jpg"))
        add(ItemMember("강혜원", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kang-hyewon.jpg"))
        add(ItemMember("히토미", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-honda-hitomi.jpg"))
        add(ItemMember("김채원", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kim-chaewon.jpg"))
        add(ItemMember("김민주", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kim-minju.jpg"))
        add(ItemMember("이채연", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-lee-chaeyeon.jpg"))
    }

    override fun onResume() {
        super.onResume()
        fcmReceiver()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mailItem_.value = arrayListOf()
        Realm.init(this)
        loadMember()
        loadMail()

        mainFab.setOnLongClickListener {
            deleteData()
            Toast.makeText(this, "deleteItem", Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }


//        (object : ItemListAdapter.onClick {
//            override fun onItemClick(v: View?, position: Int) {
//                Toast.makeText(this@MainActivity, "position:$position", Toast.LENGTH_LONG).show()
//            }
//        })
    }

    fun loadMember() {
        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        memberRy.layoutManager = layoutManager

        recyclerViewSet(memberRy, memberItem)
    }

    fun loadMail() {
        realm = Realm.getDefaultInstance()
        val allData = realm.where(MailData::class.java).findAll()
        allData.forEach {
            var profile = memberSearch(it.memberType)
            mailItem_.value?.add(ItemMail(profile, it.memberType, it.title, it.content, it.img))
        }
        Log.e("mailItem_", mailItem_.value?.size.toString())
        var layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        mailRy.layoutManager = layoutManager
        recyclerViewSet(mailRy, mailItem_.value!!)
    }

    fun deleteData() {
        realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            val result: RealmResults<MailData> =
                realm.where(MailData::class.java)
                    .findAll()
            result.deleteAllFromRealm()
        }
        mailItem_.value?.clear()
        recyclerViewSet(mailRy, mailItem_.value!!)
    }

    fun fcmReceiver() {
        if (mReceiver != null) return
        val theFilter = IntentFilter()
        theFilter.addAction(BROADCAST_MESSAGE)
        this.mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                var title = intent.getStringExtra("title")
                var body = intent.getStringExtra("body")
                var image = intent.getStringExtra("image")
                var memberType = intent.getStringExtra("memberType")
                if (intent.action == BROADCAST_MESSAGE) {
                    mailItem_.value?.add(
                        ItemMail(
                            memberSearch(memberType),
                            memberType,
                            title,
                            body,
                            image
                        )
                    )
                    recyclerViewSet(mailRy, mailItem_.value!!)
                }
            }
        }
        this.registerReceiver(this.mReceiver, theFilter)
    }

    private fun unregisterReceiver() {
        if (mReceiver != null) {
            this.unregisterReceiver(mReceiver)
            mReceiver = null
        }
    }


    private fun recyclerViewSet(ry: RecyclerView, items: ArrayList<ItemDataInterface>) {
        ItemListAdapter().apply {
            ry.adapter = this
            this.submitList(items)

            setOnItemClickListener(object : ItemListAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    Toast.makeText(this@MainActivity, "position:$position", Toast.LENGTH_SHORT).show()
                    var item = mailItem_.value?.get(position) as ItemMail
                    var intent = Intent(this@MainActivity,
                        MailDetailActivity::class.java)
                    intent.putExtra("title",item.title)
                    intent.putExtra("content",item.content)
                    intent.putExtra("img",item.img)
                    intent.putExtra("profile",memberSearch(item.name))
                    intent.putExtra("name",item.name)
                    startActivity(intent)
                }
            })
        }
    }
}
