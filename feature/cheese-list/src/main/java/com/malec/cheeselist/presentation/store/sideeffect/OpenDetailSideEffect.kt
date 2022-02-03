package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.dependencies.CheeseListOutput
import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.store.SideEffect

class OpenDetailSideEffect(
    cheeseListOutput: CheeseListOutput
) : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action -> action is CheeseListAction.OpenDetail },
    effect = { state, action ->
        action as CheeseListAction.OpenDetail
        if (state.selectedCount != 0)
            CheeseListAction.ToggleSelect(action.cheese)
        else {
            cheeseListOutput.openDetail(action.cheese.id)
            CheeseListAction.Ignore
        }
    },
    error = { CheeseListAction.Error(it) }
)