package com.malec.cheesedetail.presentation.store.sideeffect

import com.malec.cheesedetail.presentation.store.CheeseDetailAction
import com.malec.cheesedetail.presentation.store.CheeseDetailState
import com.malec.store.SideEffect

class CheckCanSaveSideEffect : SideEffect<CheeseDetailState, CheeseDetailAction>(
    requirement = { _, action ->
        action is CheeseDetailAction.SetName ||
                action is CheeseDetailAction.SetDate
    },
    effect = { state, _ ->
        val canSave = state.cheese.name.isNotBlank()
        CheeseDetailAction.SetCanSave(canSave)
    },
    error = { CheeseDetailAction.Error(it) }
)