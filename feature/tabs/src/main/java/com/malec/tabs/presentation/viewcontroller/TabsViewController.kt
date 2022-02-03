package com.malec.tabs.presentation.viewcontroller

import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.unidirectional.BaseViewController
import com.malec.tabs.presentation.Tab
import com.malec.tabs.presentation.store.TabsAction
import com.malec.tabs.presentation.store.TabsState
import com.malec.tabs.presentation.store.TabsStore

class TabsViewController(
    store: TabsStore
) : BaseViewController<TabsState, TabsAction, BaseView<TabsState>>(store) {
    override fun attach() {
        when (store.currentState.currentTab) {
            Tab.CheeseList -> onClickCheeses()
            Tab.ReportList -> onClickReports()
            else -> onClickTasks()
        }
    }

    fun onClickTasks() {
        dispatchAction(TabsAction.OpenTasks)
    }

    fun onClickCheeses() {
        dispatchAction(TabsAction.OpenCheeses)
    }

    fun onClickReports() {
        dispatchAction(TabsAction.OpenReports)
    }

    fun onClickSettings() {
        dispatchAction(TabsAction.OpenSettings)
    }

    fun onCLickLogout() {
        dispatchAction(TabsAction.Logout)
    }

    fun onClickExit() {
    }
}