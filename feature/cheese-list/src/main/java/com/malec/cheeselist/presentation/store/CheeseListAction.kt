package com.malec.cheeselist.presentation.store

import com.malec.domain.model.Cheese
import com.malec.domain.model.CheeseSort

sealed class CheeseListAction {
    object Ignore : CheeseListAction()

    object UpdateCheeses : CheeseListAction()
    data class ShowCheeses(val cheeses: List<Cheese>) : CheeseListAction()

    data class SaveCheeseTypes(val cheeseTypes: List<String>) : CheeseListAction()

    object ArchiveSelected : CheeseListAction()
    object PrintSelected : CheeseListAction()
    object DeleteSelected : CheeseListAction()
    object UnselectAll : CheeseListAction()

    data class SortBy(val sort: CheeseSort) : CheeseListAction()
    data class FilterArchived(val archived: Boolean) : CheeseListAction()
    data class FilterCheeseType(val type: String?) : CheeseListAction()
    data class FilterDateStart(val date: String) : CheeseListAction()
    data class FilterDateEnd(val date: String) : CheeseListAction()
    object ClearFilter : CheeseListAction()

    data class Search(val query: String) : CheeseListAction()

    data class Error(val throwable: Throwable) : CheeseListAction()

    data class ToggleSelect(val cheese: Cheese) : CheeseListAction()

    data class OpenDetail(val cheese: Cheese = Cheese.empty()) : CheeseListAction()

    data class Delete(val cheese: Cheese) : CheeseListAction()
}
