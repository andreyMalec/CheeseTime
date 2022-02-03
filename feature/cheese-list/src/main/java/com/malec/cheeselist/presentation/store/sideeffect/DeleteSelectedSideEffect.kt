package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.domain.usecase.CheeseUseCaseParams
import com.malec.domain.usecase.DeleteCheeseUseCase
import com.malec.store.SideEffect

class DeleteSelectedSideEffect(
    val useCase: DeleteCheeseUseCase
) : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action -> action is CheeseListAction.DeleteSelected },
    effect = { state, _ ->
        state.cheeses.filter {
            it.isSelected
        }.forEach {
            useCase.build(CheeseUseCaseParams(it))
        }
        CheeseListAction.UpdateCheeses
    },
    error = { CheeseListAction.Error(it) }
)