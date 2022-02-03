package com.malec.tasklist.presentation.store

import com.malec.domain.model.Task

sealed class TaskListAction {
    object Ignore : TaskListAction()

    object UpdateTime : TaskListAction()

    object UpdateTasks : TaskListAction()
    data class ShowTasks(val tasks: List<Task>) : TaskListAction()

    data class OpenDetail(val taskId: Long = 0L) : TaskListAction()

    data class Delete(val task: Task) : TaskListAction()

    data class Error(val throwable: Throwable) : TaskListAction()
}
