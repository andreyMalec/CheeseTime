package com.malec.cheesetime.ui.main.cheese.cheeseList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheesetime.databinding.ItemCheeseBinding
import com.malec.cheesetime.model.Cheese

class CheeseAdapter(private val vm: CheeseAction) :
    ListAdapter<Cheese, CheeseAdapter.CheeseItemViewHolder>(diffUtilCallback) {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheeseAdapter.CheeseItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCheeseBinding = ItemCheeseBinding.inflate(inflater, parent, false)
        return CheeseItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CheeseAdapter.CheeseItemViewHolder, position: Int) {
        val cheese = getItem(position)

        holder.binding?.cheese = cheese
        holder.binding?.selectMarker?.visibility = getMarkerVisibility(cheese)
    }

    inner class CheeseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemCheeseBinding? = DataBindingUtil.bind(view)

        init {
            binding?.root?.setOnClickListener {
                binding.cheese?.let {
                    vm.onClick(it)
                }
            }
            binding?.root?.setOnLongClickListener {
                binding.cheese?.let {
                    vm.onLongClick(it)
                }
                true
            }
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