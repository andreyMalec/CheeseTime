package com.malec.cheesetime.ui.main.task.taskList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.repo.TaskRepo
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val repo: TaskRepo,
    private val router: Router
) : BaseViewModel(), TaskAdapter.TaskAction {
    val taskList = MutableLiveData<List<Task>?>(null)

    private var isAutoRepeat = false

    init {
        update()
    }

    fun update() {
        safeRun {
            val tasks = repo.getAll()
            taskList.value = tasks
            repo.scheduleAll(tasks)
        }
    }

    fun autoRepeat(function: () -> Unit) {
        if (!isAutoRepeat) {
            isAutoRepeat = true
            viewModelScope.launch {
                while (true) {
                    function()
                    delay(1000 * 60)
                }
            }
        }
    }

    override fun onClick(task: Task) {
        router.navigateTo(Screens.taskManage(task))
    }

    override fun onSwipe(task: Task) {
        viewModelScope.launch {
            repo.deleteById(task.id)
            update()
        }
    }

    override fun setError(t: Throwable?) {}
}