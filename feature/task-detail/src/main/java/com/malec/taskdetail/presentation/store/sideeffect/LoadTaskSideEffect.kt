package com.malec.taskdetail.presentation.store.sideeffect

import com.malec.store.SideEffect
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState
import com.malec.taskdetail.usecase.GetTaskByIdUseCase
import com.malec.taskdetail.usecase.GetTaskByIdUseCaseParams

class LoadTaskSideEffect(
    private val useCase: GetTaskByIdUseCase
) : SideEffect<TaskDetailState, TaskDetailAction>(
    requirement = { _, action -> action is TaskDetailAction.LoadTask },
    effect = { state, action ->
        action as TaskDetailAction.LoadTask
        val task = if (state.taskId == action.taskId)
            state.task
        else
            useCase.build(GetTaskByIdUseCaseParams(action.taskId))
        if (task == null)
            TaskDetailAction.Error(NoSuchElementException())
        else
            TaskDetailAction.ShowTask(task)
    },
    error = { TaskDetailAction.Error(it) }
)