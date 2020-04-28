package com.cclss.chattering.view.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.TypedArray
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cclss.chattering.R
import com.cclss.chattering.adapter.ItemListAdapter
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemMail
import com.cclss.chattering.data.ItemMember
import com.cclss.chattering.data.source.mail.MailDataRepository
import com.cclss.chattering.util.Utils.memberSearch
import com.cclss.chattering.view.LoginActivity
import com.cclss.chattering.view.gallery.GalleryActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.messaging.FirebaseMessaging
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainContract.View {
    lateinit var realm: Realm
    private val BROADCAST_MESSAGE = "com.cclss.chattering.fcm"
    private var mReceiver: BroadcastReceiver? = null

    val memberItem by lazy {
        ArrayList<ItemDataInterface>().apply {
            add(ItemMember("IZONE","http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-iz-one.jpg", true))
            add(ItemMember("장원영","http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-jang-wonyoung.jpg", true))
            add(ItemMember("사쿠라", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-miyawaki-sakura.jpg", true))
            add(ItemMember("조유리", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-jo-yuri.jpg", true))
            add(ItemMember("나코","http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-yabuki-nako.jpg",true))
            add(ItemMember("최예나", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-choi-yena.jpg"))
            add(ItemMember("안유진", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-an-yujin.jpg"))
            add(ItemMember("권은비", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kwon-eunbi.jpg"))
            add(ItemMember("강혜원", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kang-hyewon.jpg"))
            add(ItemMember("히토미", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-honda-hitomi.jpg"))
            add(ItemMember("김채원", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kim-chaewon.jpg"))
            add(ItemMember("김민주", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-kim-minju.jpg"))
            add(ItemMember("이채연", "http://cdn.iz-one.co.kr/images/bloom-iz/v/profile-lee-chaeyeon.jpg"))
        }
    }

    private lateinit var presenter: MainPresenter

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //facesdk
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        //realm
        Realm.init(this)
        realm = Realm.getDefaultInstance()

        //fireBaseTopic
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        fcmReceiver()

        //memberRecyclerView Setting
        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        memberRy.layoutManager = layoutManager

        //mailRecyclerView Setting
        var layoutManager_ = LinearLayoutManager(this)
        layoutManager_.reverseLayout = true
        layoutManager_.stackFromEnd = true
        mailRy.layoutManager = layoutManager_

        //MailRecyclerView BttomLine
        val ATTRS: IntArray = intArrayOf(android.R.attr.listDivider)
        val a: TypedArray = this.obtainStyledAttributes(ATTRS)
        val divider = a.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen.recyclerview_padding)
        val insetDivider = InsetDrawable(divider, inset, 0, inset, 0)
        a.recycle()

        val itemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(insetDivider)
        mailRy.addItemDecoration(itemDecoration)

        //presenterSetting
        presenter = MainPresenter().apply {
            view = this@MainActivity
            memberRecyclerView = memberRy
            mailRecyclerView = mailRy
            listAdapter = ItemListAdapter()
            mailData = MailDataRepository()
            realm = this@MainActivity.realm
        }

        //loadMember,Mail
        presenter.loadMember(memberItem)
        presenter.loadMail("IZONE")

        //deleteMail
        mainFab.setOnLongClickListener {
            presenter.deleteMail()
            toastMessage("deleteItem")
            return@setOnLongClickListener true
        }
        //calendar
        maimCalendarIv.setOnClickListener {
            startActivity(Intent(this,GalleryActivity::class.java))
        }

        //LoginTest
        mainFab.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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
                var time = intent.getStringExtra("time")
                var id = intent.getIntExtra("id",0)
                if (intent.action == BROADCAST_MESSAGE) {
                    var item = ItemMail(id,memberSearch(memberType),memberType,title, body, image,time,false)
                    presenter.updateMail(item)
                }
            }
        }
        this.registerReceiver(this.mReceiver, theFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver()
    }

    private fun unregisterReceiver() {
        if (mReceiver != null) {
            this.unregisterReceiver(mReceiver)
            mReceiver = null
        }
    }

    override fun toastMessage(text : String) {
        Toast.makeText(this@MainActivity,text, Toast.LENGTH_SHORT).show()
    }
}
