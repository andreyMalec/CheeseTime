package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.domain.usecase.CheeseUseCaseParams
import com.malec.domain.usecase.DeleteCheeseUseCase
import com.malec.store.SideEffect

class DeleteSideEffect(
    private val useCase: DeleteCheeseUseCase
) : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action -> action is CheeseListAction.Delete },
    effect = { _, action ->
        action as CheeseListAction.Delete
        useCase.build(CheeseUseCaseParams(action.cheese))
        CheeseListAction.UpdateCheeses
    },
    error = { CheeseListAction.Error(it) }
)