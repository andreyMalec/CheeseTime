package com.malec.cheeselist.presentation.store

import com.malec.domain.model.CheeseFilter
import com.malec.store.Reducer

class CheeseListReducer : Reducer<CheeseListState, CheeseListAction> {
    override fun reduce(state: CheeseListState, action: CheeseListAction): CheeseListState {
        return when (action) {
            is CheeseListAction.UpdateCheeses -> state.copy(
                isLoading = true
            )
            is CheeseListAction.ShowCheeses -> state.copy(
                cheeses = action.cheeses,
                isLoading = false,
                selectedCount = state.selectedIds.size
            )
            is CheeseListAction.SaveCheeseTypes -> state.copy(
                cheeseTypes = action.cheeseTypes
            )
            is CheeseListAction.Search -> state.copy(
                searchQuery = action.query
            )
            is CheeseListAction.Error -> state.copy(
                error = action.throwable,
                isLoading = false
            )

            is CheeseListAction.ToggleSelect -> state.copy(
                selectedIds = if (state.selectedIds.contains(action.cheese.id))
                    state.selectedIds - action.cheese.id
                else
                    state.selectedIds + action.cheese.id
            )
            is CheeseListAction.UnselectAll -> state.copy(
                selectedIds = setOf()
            )

            is CheeseListAction.SortBy -> {
                state.copy(
                    filter = state.filter.copy(
                        sortBy = action.sort
                    )
                )
            }
            is CheeseListAction.FilterArchived -> {
                state.copy(
                    filter = state.filter.copy(
                        archived = action.archived
                    )
                )
            }
            is CheeseListAction.FilterCheeseType -> {
                state.copy(
                    filter = state.filter.copy(
                        type = action.type
                    )
                )
            }
            is CheeseListAction.FilterDateStart -> {
                state.copy(
                    filter = state.filter.copy(
                        dateStart = action.date
                    )
                )
            }
            is CheeseListAction.FilterDateEnd -> {
                state.copy(
                    filter = state.filter.copy(
                        dateEnd = action.date
                    )
                )
            }
            is CheeseListAction.ClearFilter -> {
                state.copy(
                    filter = CheeseFilter.empty
                )
            }

            else -> state.copy()
        }
    }
}