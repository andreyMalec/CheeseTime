package com.malec.taskdetail.presentation.store.sideeffect

import com.malec.store.SideEffect
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState

class SelectCheeseAtPositionSideEffect : SideEffect<TaskDetailState, TaskDetailAction>(
    requirement = { _, action -> action is TaskDetailAction.SelectCheeseAtPosition },
    effect = { state, action ->
        action as TaskDetailAction.SelectCheeseAtPosition
        val cheese = state.cheeses[action.position].split(" id: ")
        val cheeseId = cheese[1].toLong()
        val cheeseName = cheese[0]
        TaskDetailAction.SelectCheese(Pair(cheeseId, cheeseName))
    },
    error = { TaskDetailAction.Error(it) }
)