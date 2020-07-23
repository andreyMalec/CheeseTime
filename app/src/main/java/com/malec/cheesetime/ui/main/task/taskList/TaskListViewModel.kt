package com.malec.cheesetime.ui.main.task.taskList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.repo.TaskRepo
import com.malec.cheesetime.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class TaskListViewModel @Inject constructor(
    private val repo: TaskRepo,
    private val router: Router
) : ViewModel(), TaskAdapter.TaskAction {
    val taskList = MutableLiveData<List<Task>>(null)

    init {
        update()
    }

    fun update() {
        viewModelScope.launch {
            try {
                taskList.value = repo.getAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onClick(task: Task) {
        router.navigateTo(Screens.TaskManageScreen(task))
    }

    override fun onLongClick(task: Task) {
    }
}