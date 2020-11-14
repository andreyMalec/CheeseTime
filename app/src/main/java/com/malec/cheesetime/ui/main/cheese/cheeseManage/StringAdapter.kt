package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheesetime.databinding.ItemRemovableEdittextBinding
import com.malec.cheesetime.model.StringValue

class StringAdapter(
    private var data: MutableList<StringValue>,
    private val vm: RemovableEditTextAction
) :
    RecyclerView.Adapter<StringAdapter.StringItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StringAdapter.StringItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StringItemViewHolder(ItemRemovableEdittextBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: StringAdapter.StringItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun submitList(list: List<StringValue>?) {
        data = list?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class StringItemViewHolder(private val binding: ItemRemovableEdittextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(value: StringValue) {
            binding.value = value
        }

        init {

            binding.removeButton.setOnClickListener {
                vm.onRemoveClick(binding.value)
            }
        }
    }

    interface RemovableEditTextAction {
        fun onRemoveClick(value: StringValue?)
    }
}