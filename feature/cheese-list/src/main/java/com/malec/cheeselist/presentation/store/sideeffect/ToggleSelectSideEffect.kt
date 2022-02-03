package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.store.SideEffect

class ToggleSelectSideEffect : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action -> action is CheeseListAction.ToggleSelect },
    effect = { state, action ->
        action as CheeseListAction.ToggleSelect
        val updated = state.cheeses.map {
            if (it.id == action.cheese.id)
                it.toggleSelect()
            else
                it
        }
        CheeseListAction.ShowCheeses(updated)
    },
    error = { CheeseListAction.Error(it) }
)