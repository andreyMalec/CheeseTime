package com.malec.cheesedetail.presentation.store.sideeffect

import com.malec.cheesedetail.presentation.store.CheeseDetailAction
import com.malec.cheesedetail.presentation.store.CheeseDetailState
import com.malec.cheesedetail.usecase.GetCheeseByIdUseCase
import com.malec.cheesedetail.usecase.GetCheeseByIdUseCaseParams
import com.malec.store.SideEffect

class LoadCheeseSideEffect(
    private val useCase: GetCheeseByIdUseCase
) : SideEffect<CheeseDetailState, CheeseDetailAction>(
    requirement = { _, action -> action is CheeseDetailAction.LoadCheese },
    effect = { state, action ->
        action as CheeseDetailAction.LoadCheese
        val cheese = if (state.cheeseId == action.cheeseId)
            state.cheese
        else
            useCase.build(GetCheeseByIdUseCaseParams(action.cheeseId))
        if (cheese == null)
            CheeseDetailAction.Error(NoSuchElementException())
        else
            CheeseDetailAction.ShowCheese(cheese)
    },
    error = { CheeseDetailAction.Error(it) }
)