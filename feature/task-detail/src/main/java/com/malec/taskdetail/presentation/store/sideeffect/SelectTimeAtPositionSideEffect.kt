package com.malec.taskdetail.presentation.store.sideeffect

import com.malec.store.SideEffect
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState
import com.malec.taskdetail.usecase.GetTimeAtPositionUseCase
import com.malec.taskdetail.usecase.GetTimeAtPositionUseCaseParams

class SelectTimeAtPositionSideEffect(
    useCase: GetTimeAtPositionUseCase
) : SideEffect<TaskDetailState, TaskDetailAction>(
    requirement = { _, action -> action is TaskDetailAction.SelectTimeAtPosition },
    effect = { state, action ->
        action as TaskDetailAction.SelectTimeAtPosition
        val offset = useCase.build(GetTimeAtPositionUseCaseParams(action.position))
        val time = state.task.date + offset
        TaskDetailAction.SetDateTime(time)
    },
    error = { TaskDetailAction.Error(it) }
)