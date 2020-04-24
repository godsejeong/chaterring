package com.cclss.chattering.data

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cclss.chattering.R
import de.hdodenhof.circleimageview.CircleImageView

object BaseViewHolder {
    class MemberViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var image = view.findViewById(R.id.itemMemberImg) as CircleImageView
        var name = view.findViewById(R.id.itemMemberTv) as TextView
    }

    class MailViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var image = view.findViewById(R.id.itemMailImg) as CircleImageView
        var title = view.findViewById(R.id.itemMailTitle) as TextView
        var content = view.findViewById(R.id.itemMailContent) as TextView
        var name = view.findViewById(R.id.itemMailName) as TextView
        var time = view.findViewById(R.id.itemMailTime) as TextView
        var checkImg = view.findViewById(R.id.itemMailCheckImg) as ImageView
    }
}