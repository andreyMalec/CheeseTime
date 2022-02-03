package com.malec.cheeselist.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.malec.cheeselist.databinding.ItemCheeseBinding
import com.malec.domain.model.Cheese
import com.malec.presentation.base.BindingListAdapter

class CheeseAdapter(private val vm: CheeseAction) :
    BindingListAdapter<Cheese, ItemCheeseBinding>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Cheese>() {
            override fun areItemsTheSame(oldItem: Cheese, newItem: Cheese): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cheese, newItem: Cheese): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemCheeseBinding.inflate(inflater, parent, false)

    override fun onBindViewHolder(binding: ItemCheeseBinding, position: Int) {
        val cheese = getItem(position)

        binding.cheese = cheese
        binding.selectMarker.visibility = getMarkerVisibility(cheese)
    }

    override fun onCreateViewHolder(binding: ItemCheeseBinding) {
        binding.root.setOnClickListener {
            binding.cheese?.let {
                vm.onClick(it)
            }
        }

        binding.root.setOnLongClickListener {
            binding.cheese?.let {
                vm.onLongClick(it)
            }
            true
        }
    }

    private fun getMarkerVisibility(cheese: Cheese): Int {
        return if (cheese.isSelected)
            View.VISIBLE
        else
            View.INVISIBLE
    }

    interface CheeseAction {
        fun onClick(cheese: Cheese)

        fun onLongClick(cheese: Cheese)
    }
}