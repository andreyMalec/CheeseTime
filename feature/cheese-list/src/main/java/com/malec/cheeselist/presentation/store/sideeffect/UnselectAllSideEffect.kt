package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.store.SideEffect

class UnselectAllSideEffect : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action -> action is CheeseListAction.UnselectAll },
    effect = { state, _ ->
        val updated = state.cheeses.map {
            if (it.isSelected)
                it.toggleSelect()
            else
                it
        }
        CheeseListAction.ShowCheeses(updated)
    },
    error = { CheeseListAction.Error(it) }
)