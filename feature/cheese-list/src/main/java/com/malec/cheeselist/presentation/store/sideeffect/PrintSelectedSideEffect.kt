package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.domain.usecase.ShareCheesesUseCase
import com.malec.domain.usecase.ShareCheesesUseCaseParams
import com.malec.store.SideEffect

class PrintSelectedSideEffect(
    val useCase: ShareCheesesUseCase
) : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action -> action is CheeseListAction.PrintSelected },
    effect = { state, _ ->
        useCase.build(ShareCheesesUseCaseParams(state.cheeses.filter { it.isSelected }))
        CheeseListAction.Ignore
    },
    error = { CheeseListAction.Error(it) }
)