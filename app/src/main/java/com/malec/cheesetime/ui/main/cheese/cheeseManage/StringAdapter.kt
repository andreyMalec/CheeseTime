package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.malec.cheesetime.databinding.ItemRemovableEdittextBinding
import com.malec.cheesetime.model.StringValue
import com.malec.cheesetime.ui.BindingListAdapter

class StringAdapter(private val vm: RemovableEditTextAction) :
    BindingListAdapter<StringValue, ItemRemovableEdittextBinding>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<StringValue>() {
            override fun areItemsTheSame(oldItem: StringValue, newItem: StringValue): Boolean {
                return oldItem.data == newItem.data
            }

            override fun areContentsTheSame(oldItem: StringValue, newItem: StringValue): Boolean {
                return oldItem.data == newItem.data
            }
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemRemovableEdittextBinding.inflate(inflater, parent, false)

    override fun onBindViewHolder(
        holder: BindingListAdapter<StringValue, ItemRemovableEdittextBinding>.ItemViewHolder,
        position: Int
    ) {
        val value = getItem(position)

        holder.binding.value = value
    }

    override fun onCreateViewHolder(binding: ItemRemovableEdittextBinding) {
        binding.removeButton.setOnClickListener {
            binding.value?.let {
                vm.onRemoveClick(it)
            }
        }
    }

    interface RemovableEditTextAction {
        fun onRemoveClick(value: StringValue)
    }
}