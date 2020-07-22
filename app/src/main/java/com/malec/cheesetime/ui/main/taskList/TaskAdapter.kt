package com.malec.cheesetime.ui.main.taskList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malec.cheesetime.databinding.ItemTaskBinding
import com.malec.cheesetime.model.Task

class TaskAdapter(private val vm: TaskAction) :
    ListAdapter<Task, TaskAdapter.TaskItemViewHolder>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.cheeseId == newItem.cheeseId &&
                        oldItem.cheeseName == newItem.cheeseName &&
                        oldItem.todo == newItem.todo &&
                        oldItem.date == newItem.date &&
                        oldItem.comment == newItem.comment
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTaskBinding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val task = getItem(position)

        holder.binding?.task = task
    }

    inner class TaskItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemTaskBinding? = DataBindingUtil.bind(view)

        init {
            binding?.root?.setOnClickListener {
                binding.task?.let {
                    vm.onClick(it)
                }
            }
            binding?.root?.setOnLongClickListener {
                binding.task?.let {
                    vm.onLongClick(it)
                }
                true
            }
        }
    }

    interface TaskAction {
        fun onClick(task: Task)

        fun onLongClick(task: Task)
    }
}