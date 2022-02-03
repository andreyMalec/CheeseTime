package com.malec.taskdetail.presentation.store.sideeffect

import com.malec.domain.usecase.TaskUseCaseParams
import com.malec.store.SideEffect
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState
import com.malec.taskdetail.usecase.SaveTaskUseCase

class SaveTaskSideEffect(
    private val useCase: SaveTaskUseCase
) : SideEffect<TaskDetailState, TaskDetailAction>(
    requirement = { _, action -> action is TaskDetailAction.SaveTask },
    effect = { state, _ ->
        useCase.build(TaskUseCaseParams(state.task))
        TaskDetailAction.Back
    },
    error = { TaskDetailAction.Error(it) }
)