package com.malec.taskdetail.presentation.viewcontroller

import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.unidirectional.BaseViewController
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState
import com.malec.taskdetail.presentation.store.TaskDetailStore

class TaskDetailViewController(
    store: TaskDetailStore
) : BaseViewController<TaskDetailState, TaskDetailAction, BaseView<TaskDetailState>>(store) {
    fun loadTask(id: Long) {
        dispatchAction(TaskDetailAction.LoadTask(id))
    }

    fun selectCheeseAtPosition(position: Int) {
        dispatchAction(TaskDetailAction.SelectCheeseAtPosition(position))
    }

    fun setTodo(todo: String) {
        dispatchAction(TaskDetailAction.SetTodo(todo))
    }

    fun setComment(comment: String) {
        dispatchAction(TaskDetailAction.SetComment(comment))
    }

    fun setTime(time: String) {
        dispatchAction(TaskDetailAction.SetTime(time))
    }

    fun setDate(date: String) {
        dispatchAction(TaskDetailAction.SetDate(date))
    }

    fun selectTimeAtPosition(position: Int) {
        dispatchAction(TaskDetailAction.SelectTimeAtPosition(position))
    }

    fun saveTask() {
        dispatchAction(TaskDetailAction.SaveTask)
    }

    fun deleteTask() {
        dispatchAction(TaskDetailAction.DeleteTask)
    }

    fun onBack() {
        dispatchAction(TaskDetailAction.Back)
    }
}