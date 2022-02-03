package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.domain.usecase.CheeseUseCaseParams
import com.malec.domain.usecase.UpdateCheeseUseCase
import com.malec.store.SideEffect

class ArchiveSelectedSideEffect(
    val useCase: UpdateCheeseUseCase
) : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action -> action is CheeseListAction.ArchiveSelected },
    effect = { state, _ ->
        state.cheeses.filter {
            it.isSelected
        }.forEach {
            useCase.build(CheeseUseCaseParams(it.toggleArchive()))
        }
        CheeseListAction.UpdateCheeses
    },
    error = { CheeseListAction.Error(it) }
)