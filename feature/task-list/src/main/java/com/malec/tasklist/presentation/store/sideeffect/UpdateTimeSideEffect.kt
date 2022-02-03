package com.malec.tasklist.presentation.store.sideeffect

import com.malec.store.SideEffect
import com.malec.tasklist.presentation.store.TaskListAction
import com.malec.tasklist.presentation.store.TaskListState

class UpdateTimeSideEffect : SideEffect<TaskListState, TaskListAction>(
    requirement = { _, action -> action is TaskListAction.UpdateTime },
    effect = { state, _ ->
        TaskListAction.ShowTasks(state.tasks)
    },
    error = { TaskListAction.Error(it) }
)