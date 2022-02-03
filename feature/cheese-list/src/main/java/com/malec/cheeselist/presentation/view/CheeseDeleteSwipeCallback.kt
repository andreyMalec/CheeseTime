package com.malec.cheeselist.presentation.view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheeselist.databinding.ItemCheeseBinding
import com.malec.presentation.DeleteSwipeCallback
import com.malec.presentation.base.BindingListAdapter

class CheeseDeleteSwipeCallback(
    listener: OnSwipeListener,
    context: Context
) : DeleteSwipeCallback(listener, context) {
    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        //block swipe for selected items
        return if (viewHolder is BindingListAdapter<*, *>.ItemViewHolder &&
            viewHolder.binding is ItemCheeseBinding &&
            (viewHolder.binding as ItemCheeseBinding).selectMarker.visibility == View.VISIBLE
        )
            0
        else
            super.getSwipeDirs(recyclerView, viewHolder)
    }
}