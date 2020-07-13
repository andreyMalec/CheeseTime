package com.malec.cheesetime.ui.main.cheeseList

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
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Cheese, newItem: Cheese): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.date == newItem.date &&
                        oldItem.recipe == newItem.recipe &&
                        oldItem.comment == newItem.comment &&
                        oldItem.milk == newItem.milk &&
                        oldItem.composition == newItem.composition &&
                        oldItem.stages == newItem.stages &&
                        oldItem.badgeColor == newItem.badgeColor
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
    }


    inner class CheeseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemCheeseBinding? = DataBindingUtil.bind(view)

        init {
            binding?.root?.setOnClickListener { v ->
                binding.cheese?.let {
                    vm.editCheese(it)
                }
            }
        }
    }

    interface CheeseAction {
        fun deleteCheese(cheese: Cheese)

        fun editCheese(cheese: Cheese)
    }
}