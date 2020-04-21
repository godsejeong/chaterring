package com.cclss.chattering.adapter


import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.cclss.chattering.R
import com.cclss.chattering.data.BaseViewHolder
import com.cclss.chattering.data.ItemDataInterface
import com.cclss.chattering.data.ItemDataState.MAIL
import com.cclss.chattering.data.ItemDataState.MEMBER
import com.cclss.chattering.data.ItemMail
import com.cclss.chattering.data.ItemMember


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
                    .inflate(R.layout.member_item, parent, false)
                BaseViewHolder.MemberViewHolder(layout)
            }

            MAIL -> {
                var layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.mail_message_item, parent, false)
                BaseViewHolder.MailViewHolder(layout)
            }

            else -> null!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        when (getItemViewType(position)) {
            MEMBER -> {
                val item = getItem(position) as ItemMember
                var holder = (holder as BaseViewHolder.MemberViewHolder)
                Glide.with(context)
                    .load(item.img)
                    .listener(createLoggerListener("original"))
                    .override(150, 150)
                    .into(holder.image)

                holder.name.text = item.name

                if (item.isSubscription)
                    holder.image.borderColor = context.getColor(R.color.colorSeleteMember)
                else
                    holder.image.borderColor = context.getColor(R.color.transparent)
            }

            MAIL -> {
                val item = getItem(position) as ItemMail
                val holder = (holder as BaseViewHolder.MailViewHolder)

                Glide.with(context)
                    .load(item.profile)
                    .listener(createLoggerListener("original"))
                    .override(200, 200)
                    .into(holder.image)

                holder.title.text = item.title
                holder.content.text = item.content
                holder.image.borderColor = context.getColor(R.color.transparent)

                if (itemClick != null) {
                    holder.itemView.setOnClickListener {
                        if (position != RecyclerView.NO_POSITION) {
                            itemClick!!.onClick(it, position)
                        }
                    }
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItemId(position).hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItem()
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

    private fun createLoggerListener(name: String): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource is BitmapDrawable) {
                    val bitmap = resource.bitmap
                    Log.d(
                        "GlideApp",
                        String.format(
                            "Ready %s bitmap %,d bytes, size: %d x %d",
                            name,
                            bitmap.byteCount,
                            bitmap.width,
                            bitmap.height
                        )
                    )
                }
                return false
            }
        }
    }
}


