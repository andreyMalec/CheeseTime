package com.malec.taskdetail.presentation.store.sideeffect

import com.malec.store.SideEffect
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState

class CheckCanSaveSideEffect : SideEffect<TaskDetailState, TaskDetailAction>(
    requirement = { _, action ->
        action is TaskDetailAction.SetTodo || action is TaskDetailAction.SetDate
                || action is TaskDetailAction.SetTime || action is TaskDetailAction.SetDateTime
    },
    effect = { state, _ ->
        val canSave = !(state.task.todo.isBlank() || state.date.isBlank() || state.time.isBlank())
        TaskDetailAction.SetCanSave(canSave)
    },
    error = { TaskDetailAction.Error(it) }
)