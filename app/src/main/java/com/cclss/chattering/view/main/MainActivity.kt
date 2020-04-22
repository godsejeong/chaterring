package com.cclss.chattering.view.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cclss.chattering.R
import com.cclss.chattering.Utils.memberSearch
import com.cclss.chattering.adapter.ItemListAdapter
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemMail
import com.cclss.chattering.data.ItemMember
import com.cclss.chattering.data.source.mail.MailDataRepository
import com.google.firebase.messaging.FirebaseMessaging
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainContract.View {
    lateinit var realm: Realm
    private val BROADCAST_MESSAGE = "com.cclss.chattering.fcm"
    private var mReceiver: BroadcastReceiver? = null

    val memberItem by lazy{
        ArrayList<ItemDataInterface>().apply {
            add(ItemMember("장원영","http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-jang-wonyoung.jpg", true))
            add(ItemMember("사쿠라", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-miyawaki-sakura.jpg", true))
            add(ItemMember("조유리", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-jo-yuri.jpg", true))
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
    }

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        fcmReceiver()

        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        memberRy.layoutManager = layoutManager

        var layoutManager_ = LinearLayoutManager(this)
        layoutManager_.reverseLayout = true
        layoutManager_.stackFromEnd = true
        mailRy.layoutManager = layoutManager_

        presenter = MainPresenter().apply {
            view = this@MainActivity
            memberRecyclerView = memberRy
            mailRecyclerView = mailRy
            listAdapter = ItemListAdapter()
            mailData = MailDataRepository()
        }

        presenter.loadMember(memberItem)

        presenter.loadMail(realm)

        mainFab.setOnLongClickListener {
            presenter.deleteMail(realm)
            toastMessage("deleteItem")
            return@setOnLongClickListener true
        }
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
                    var item = ItemMail(memberSearch(memberType),memberType,title, body, image)
                    presenter.updateMail(item)
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

    override fun onDestroy() {
        unregisterReceiver()
        super.onDestroy()
    }

    override fun toastMessage(text : String) {
        Toast.makeText(this@MainActivity,text, Toast.LENGTH_SHORT).show()
    }
}
