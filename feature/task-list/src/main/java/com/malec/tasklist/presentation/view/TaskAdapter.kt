package com.malec.tasklist.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.malec.domain.model.Task
import com.malec.presentation.base.BindingListAdapter
import com.malec.tasklist.databinding.ItemTaskBinding

class TaskAdapter(private val vm: TaskAction) :
    BindingListAdapter<Task, ItemTaskBinding>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemTaskBinding.inflate(inflater, parent, false)

    override fun onBindViewHolder(binding: ItemTaskBinding, position: Int) {
        val task = getItem(position)

        binding.task = task
    }

    override fun onCreateViewHolder(binding: ItemTaskBinding) {
        binding.root.setOnClickListener {
            binding.task?.let {
                vm.onClick(it)
            }
        }
    }

    interface TaskAction {
        fun onClick(task: Task)

        fun onSwipe(task: Task)
    }
}