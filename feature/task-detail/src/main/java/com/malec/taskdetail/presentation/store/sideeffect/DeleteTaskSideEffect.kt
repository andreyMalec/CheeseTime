package com.malec.taskdetail.presentation.store.sideeffect

import com.malec.domain.usecase.DeleteTaskUseCase
import com.malec.domain.usecase.TaskUseCaseParams
import com.malec.store.SideEffect
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState

class DeleteTaskSideEffect(
    private val useCase: DeleteTaskUseCase
) : SideEffect<TaskDetailState, TaskDetailAction>(
    requirement = { _, action -> action is TaskDetailAction.DeleteTask },
    effect = { state, _ ->
        useCase.build(TaskUseCaseParams(state.task))
        TaskDetailAction.Back
    },
    error = { TaskDetailAction.Error(it) }
)