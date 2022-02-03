package com.malec.cheeselist.presentation.viewcontroller

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.cheeselist.presentation.store.CheeseListStore
import com.malec.cheeselist.presentation.view.CheeseAdapter
import com.malec.domain.model.Cheese
import com.malec.domain.model.CheeseSort
import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.unidirectional.BaseViewController

class CheeseListViewController(
    store: CheeseListStore
) : BaseViewController<CheeseListState, CheeseListAction, BaseView<CheeseListState>>(store),
    CheeseAdapter.CheeseAction {

    override fun firstViewAttach() {
        onUpdateCheeses()
    }

    fun deleteSelected() {
        dispatchAction(CheeseListAction.DeleteSelected)
    }

    fun printSelected() {
        dispatchAction(CheeseListAction.PrintSelected)
    }

    fun archiveSelected() {
        dispatchAction(CheeseListAction.ArchiveSelected)
    }

    fun unselectAll() {
        dispatchAction(CheeseListAction.UnselectAll)
    }

    fun onSearch(query: String) {
        dispatchAction(CheeseListAction.Search(query))
    }

    fun sortBy(sort: CheeseSort) {
        dispatchAction(CheeseListAction.SortBy(sort))
    }

    fun filterArchived(isArchived: Boolean) {
        dispatchAction(CheeseListAction.FilterArchived(isArchived))
    }

    fun filterCheeseType(type: String?) {
        dispatchAction(CheeseListAction.FilterCheeseType(type))
    }

    fun filterDateStart(date: String) {
        dispatchAction(CheeseListAction.FilterDateStart(date))
    }

    fun filterDateEnd(date: String) {
        dispatchAction(CheeseListAction.FilterDateEnd(date))
    }

    fun clearFilter() {
        dispatchAction(CheeseListAction.ClearFilter)
    }

    fun onUpdateCheeses() {
        dispatchAction(CheeseListAction.UpdateCheeses)
    }

    fun onSwipe(cheese: Cheese) {
        dispatchAction(CheeseListAction.Delete(cheese))
    }

    override fun onClick(cheese: Cheese) {
        dispatchAction(CheeseListAction.OpenDetail(cheese))
    }

    fun onAddClick() {
        dispatchAction(CheeseListAction.OpenDetail())
    }

    override fun onLongClick(cheese: Cheese) {
        dispatchAction(CheeseListAction.ToggleSelect(cheese))
    }

    fun onExitClick() {
    }
}