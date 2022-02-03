package com.malec.tasklist.presentation.store.sideeffect

import com.malec.domain.usecase.DeleteTaskUseCase
import com.malec.domain.usecase.TaskUseCaseParams
import com.malec.store.SideEffect
import com.malec.tasklist.presentation.store.TaskListAction
import com.malec.tasklist.presentation.store.TaskListState

class DeleteSideEffect(
    private val useCase: DeleteTaskUseCase
) : SideEffect<TaskListState, TaskListAction>(
    requirement = { _, action -> action is TaskListAction.Delete },
    effect = { _, action ->
        action as TaskListAction.Delete
        useCase.build(TaskUseCaseParams(action.task))
        TaskListAction.UpdateTasks
    },
    error = { TaskListAction.Error(it) }
)