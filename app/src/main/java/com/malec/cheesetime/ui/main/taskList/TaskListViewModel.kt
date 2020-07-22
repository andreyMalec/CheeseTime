package com.malec.cheesetime.ui.main.taskList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.repo.TaskRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val repo: TaskRepo
) : ViewModel(), TaskAdapter.TaskAction {
    val taskList = MutableLiveData<List<Task>>(null)

    init {
        update()
    }

    fun update() {
        viewModelScope.launch {
            taskList.value = repo.getAll()
        }
    }

    override fun onClick(task: Task) {
    }

    override fun onLongClick(task: Task) {
    }
}