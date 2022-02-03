package com.malec.tasklist.presentation.viewcontroller

import android.os.Handler
import android.os.Looper
import com.malec.domain.model.Task
import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.unidirectional.BaseViewController
import com.malec.tasklist.presentation.store.TaskListAction
import com.malec.tasklist.presentation.store.TaskListState
import com.malec.tasklist.presentation.store.TaskListStore

class TaskListViewController(
    store: TaskListStore
) : BaseViewController<TaskListState, TaskListAction, BaseView<TaskListState>>(store) {

    override fun attach() {
        onUpdateTasks()
        loop()
    }

    private fun updateTime() {
        dispatchAction(TaskListAction.UpdateTime)
        if (isAttach)
            loop()
    }

    private fun loop() {
        Handler(Looper.getMainLooper()).postDelayed({
            updateTime()
        }, 60 * 1000)
    }

    fun onUpdateTasks() {
        dispatchAction(TaskListAction.UpdateTasks)
    }

    fun onTaskClick(task: Task) {
        dispatchAction(TaskListAction.OpenDetail(task.id))
    }

    fun onAddClick() {
        dispatchAction(TaskListAction.OpenDetail())
    }

    fun onSwipe(task: Task) {
        dispatchAction(TaskListAction.Delete(task))
    }

    fun onExitClick() {
    }
}