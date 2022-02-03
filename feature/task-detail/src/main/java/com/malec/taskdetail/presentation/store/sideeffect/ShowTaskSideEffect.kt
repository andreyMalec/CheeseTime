package com.malec.taskdetail.presentation.store.sideeffect

import com.malec.interactor.EmptyParams
import com.malec.store.SideEffect
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState
import com.malec.taskdetail.usecase.GetCheesesUseCase

class ShowTaskSideEffect(
    private val useCase: GetCheesesUseCase
) : SideEffect<TaskDetailState, TaskDetailAction>(
    requirement = { state, action -> action is TaskDetailAction.ShowTask && state.isUpdateCheeses },
    effect = { _, action ->
        action as TaskDetailAction.ShowTask
        val cheeses = useCase.build(EmptyParams).toMutableList()
        if (action.task.id == 0L)
            TaskDetailAction.ShowCheeses(cheeses)
        else {
            val currentCheese = "${action.task.cheeseName} id: ${action.task.cheeseId}"
            cheeses.remove(currentCheese)
            TaskDetailAction.ShowCheeses(listOf(currentCheese) + cheeses)
        }
    },
    error = { TaskDetailAction.Error(it) }
)