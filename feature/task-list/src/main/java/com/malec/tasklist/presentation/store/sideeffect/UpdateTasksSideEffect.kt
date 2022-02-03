package com.malec.tasklist.presentation.store.sideeffect

import com.malec.interactor.EmptyParams
import com.malec.store.SideEffect
import com.malec.tasklist.presentation.store.TaskListAction
import com.malec.tasklist.presentation.store.TaskListState
import com.malec.tasklist.usecase.GetTasksUseCase

class UpdateTasksSideEffect(
    useCase: GetTasksUseCase
) : SideEffect<TaskListState, TaskListAction>(
    requirement = { _, action -> action is TaskListAction.UpdateTasks },
    effect = { _, _ ->
        TaskListAction.ShowTasks(useCase.build(EmptyParams))
    },
    error = { TaskListAction.Error(it) }
)