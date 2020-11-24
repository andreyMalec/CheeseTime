package com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.malec.cheesetime.databinding.ItemFullscreenPhotoBinding
import com.malec.cheesetime.model.Photo

class FullscreenPhotoAdapter(
    private val items: List<Photo>,
    private val listener: OnPhotoClickListener
) : PagerAdapter() {
    override fun instantiateItem(parent: ViewGroup, position: Int): View {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemFullscreenPhotoBinding = ItemFullscreenPhotoBinding.inflate(
            inflater,
            parent,
            false
        )
        binding.photo = items[position]
        parent.addView(binding.root)
        binding.root.setOnClickListener {
            listener.onClick()
        }
        return binding.root
    }

    override fun destroyItem(parent: ViewGroup, position: Int, view: Any) {
        parent.removeView(view as View?)
    }

    override fun getCount() = items.size

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    fun interface OnPhotoClickListener {
        fun onClick()
    }
}