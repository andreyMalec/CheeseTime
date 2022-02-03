package com.malec.taskdetail.presentation.store

import com.malec.domain.model.Task

sealed class TaskDetailAction {
    object Back : TaskDetailAction()

    data class LoadTask(val taskId: Long) : TaskDetailAction()
    data class ShowTask(val task: Task) : TaskDetailAction()

    data class SelectCheeseAtPosition(val position: Int) : TaskDetailAction()
    data class SelectCheese(val cheese: Pair<Long, String>) : TaskDetailAction()
    data class SetTodo(val todo: String) : TaskDetailAction()
    data class SetComment(val comment: String) : TaskDetailAction()
    data class SetCanSave(val canSave: Boolean) : TaskDetailAction()
    data class SetDateTime(val dateTime: Long) : TaskDetailAction()
    data class SetDate(val date: String) : TaskDetailAction()
    data class SetTime(val time: String) : TaskDetailAction()
    data class SelectTimeAtPosition(val position: Int) : TaskDetailAction()

    data class ShowCheeses(val cheeses: List<String>) : TaskDetailAction()

    object DeleteTask : TaskDetailAction()

    object SaveTask : TaskDetailAction()

    data class Error(val throwable: Throwable) : TaskDetailAction()
}
