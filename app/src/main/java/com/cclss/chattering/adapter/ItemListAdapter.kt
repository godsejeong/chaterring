package com.cclss.chattering.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cclss.chattering.R
import com.cclss.chattering.data.*
import com.cclss.chattering.data.ItemDataState.GALLERY
import com.cclss.chattering.data.ItemDataState.GALLERY_DETAIL
import com.cclss.chattering.data.ItemDataState.MAIL
import com.cclss.chattering.data.ItemDataState.MEMBER
import org.joda.time.DateTime
import java.util.*


class ItemListAdapter : ListAdapter<ItemDataInterface, RecyclerView.ViewHolder>(itemCallback) {

    private var itemClick: ItemClick? = null

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: ItemClick?) {
        itemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            MEMBER -> {
                var layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_member, parent, false)
                BaseViewHolder.MemberViewHolder(layout)
            }

            MAIL -> {
                var layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_mail_message, parent, false)
                BaseViewHolder.MailViewHolder(layout)
            }

            GALLERY -> {
                var layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_gallery,parent,false)
                BaseViewHolder.GalleryViewHolder(layout)
            }

            GALLERY_DETAIL ->{
                var layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_gallery_detail,parent,false)
                BaseViewHolder.GalleryDetailViewHolder(layout)
            }

            else -> null!!
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        when (getItemViewType(position)) {
            MEMBER -> {
                val item = getItem(position) as ItemMember
                var holder = (holder as BaseViewHolder.MemberViewHolder)
                Glide.with(context)
                    .load(item.img)
                    .override(150, 150)
                    .into(holder.image)

                holder.name.text = item.name

                if (item.isSubscription)
                    holder.image.borderColor = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        context.resources.getColor(R.color.colorSeleteMember)
                    } else {
                        context.getColor(R.color.colorSeleteMember)
                    }
                else
                    holder.image.borderColor = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        context.resources.getColor(R.color.transparent)
                    } else {
                        context.getColor(R.color.transparent)
                    }
            }

            MAIL -> {
                val item = getItem(position) as ItemMail
                val holder = (holder as BaseViewHolder.MailViewHolder)

                Glide.with(context)
                    .load(item.profile)
                    .override(120, 120)
                    .into(holder.image)

                holder.title.text = item.title
                holder.content.text = item.content
                holder.name.text = item.name

                var time = item.time
                val day : String = time!!.substring(time.lastIndexOf("/") + 1)
                val onlyTime : String = time.substring(time.lastIndexOf(")")+1,time.lastIndexOf("/")).trim()

                holder.time.text =
                    if (day == getRealDay())
                        onlyTime
                    else
                        day

                if (item.isCheck)
                    holder.checkImg.visibility = View.GONE
                else
                    holder.checkImg.visibility = View.VISIBLE

                if (itemClick != null) {
                    holder.itemView.setOnClickListener {
                        if (position != RecyclerView.NO_POSITION) {
                            itemClick!!.onClick(it, position)
                        }
                    }
                }
            }

            GALLERY->{
                val item = getItem(position) as ItemGallery
                var holder = (holder as BaseViewHolder.GalleryViewHolder)

                Glide.with(context)
                    .asBitmap()
                    .load(item.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image)


                holder.title.text = item.title
                holder.index.text = "${item.index}장"

                if (itemClick != null) {
                    holder.itemView.setOnClickListener {
                        if (position != RecyclerView.NO_POSITION) {
                            itemClick!!.onClick(it, position)
                        }
                    }
                }
            }

            GALLERY_DETAIL->{
                val item = getItem(position) as ItemGalleryDetail
                var holder = (holder as BaseViewHolder.GalleryDetailViewHolder)

                Glide.with(context)
                    .asBitmap()
                    .load(item.img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItemId(position).hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItem()
    }

    fun getRealDay(): String {
        val dateTime = DateTime()
        return dateTime.toString("MM.dd", Locale.KOREA)
    }

    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<ItemDataInterface>() {
            override fun areItemsTheSame(
                oldItemData: ItemDataInterface,
                newItemData: ItemDataInterface
            ): Boolean {
                return oldItemData == newItemData
            }

            override fun areContentsTheSame(
                oldItemData: ItemDataInterface,
                newItemData: ItemDataInterface
            ): Boolean {
                return oldItemData == newItemData
            }
        }
    }
}


