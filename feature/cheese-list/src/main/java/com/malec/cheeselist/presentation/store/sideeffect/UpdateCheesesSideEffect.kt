package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.cheeselist.usecase.SearchUseCaseParams
import com.malec.cheeselist.usecase.UpdateCheesesUseCase
import com.malec.store.SideEffect

class UpdateCheesesSideEffect(
    useCase: UpdateCheesesUseCase
) : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action -> action is CheeseListAction.UpdateCheeses },
    effect = { state, _ ->
        CheeseListAction.ShowCheeses(
            useCase.build(
                SearchUseCaseParams(
                    state.searchQuery,
                    state.filter,
                    state.selectedIds
                )
            )
        )
    },
    error = { CheeseListAction.Error(it) }
)