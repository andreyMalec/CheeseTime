package com.malec.cheesetime.ui.main

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheesetime.R
import com.malec.cheesetime.ui.main.cheese.cheeseList.CheeseAdapter
import kotlin.math.abs

class DeleteSwipeCallback(
    private val listener: OnSwipeListener,
    private val context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.icon_delete)

    private var vibrate = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder.adapterPosition)
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        //block swipe for selected items
        return if (viewHolder is CheeseAdapter.CheeseItemViewHolder &&
            viewHolder.binding?.selectMarker?.visibility == View.VISIBLE
        )
            0
        else
            super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        vibrate(c.width.toFloat(), dX)

        drawBackgroundIcon(c, viewHolder.itemView)
    }

    private fun vibrate(maxWidth: Float, dX: Float) {
        val a = 0F
        val b = maxWidth / 2.2F
        if (abs(dX) in a..b)
            vibrate = true

        if (abs(dX) >= maxWidth / 2F && vibrate)
            context.getSystemService(Context.VIBRATOR_SERVICE)?.let {
                it as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    it.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                else
                    it.vibrate(30)

                vibrate = false
            }
    }

    private fun drawBackgroundIcon(c: Canvas, itemView: View) {
        if (deleteIcon == null) return

        val top = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
        val left = itemView.width - deleteIcon.intrinsicWidth * 2
        val right = left + deleteIcon.intrinsicWidth
        val bottom = top + deleteIcon.intrinsicHeight

        deleteIcon.setBounds(left, top, right, bottom)

        deleteIcon.draw(c)
    }

    fun interface OnSwipeListener {
        fun onSwiped(position: Int)
    }
}