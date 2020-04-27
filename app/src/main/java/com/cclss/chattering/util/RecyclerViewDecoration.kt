package com.cclss.chattering.util

import android.content.Context
import android.graphics.Rect
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class RecyclerViewDecoration(context : Context,@DimenRes divWidth : Int,@DimenRes divHeight : Int) : RecyclerView.ItemDecoration() {
    var divWidth =0
    var divHeight = 0
    init {
        this.divWidth = context.resources.getDimensionPixelSize(divWidth)
        this.divHeight = context.resources.getDimensionPixelSize(divHeight)
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        super.getItemOffsets(outRect, itemPosition, parent)
        outRect.set(divHeight,divHeight,divWidth,divWidth)
    }
}