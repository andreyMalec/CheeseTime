package com.malec.cheeselist.presentation.store.sideeffect

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.cheeselist.usecase.SearchUseCaseParams
import com.malec.cheeselist.usecase.UpdateCheesesUseCase
import com.malec.store.SideEffect

class FilterSideEffect(
    useCase: UpdateCheesesUseCase
) : SideEffect<CheeseListState, CheeseListAction>(
    requirement = { _, action ->
        action is CheeseListAction.SortBy ||
                action is CheeseListAction.FilterArchived ||
                action is CheeseListAction.FilterCheeseType ||
                action is CheeseListAction.FilterDateStart ||
                action is CheeseListAction.FilterDateEnd ||
                action is CheeseListAction.ClearFilter
    },
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