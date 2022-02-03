package com.malec.tabs.presentation.store

import com.malec.store.Reducer
import com.malec.tabs.presentation.Tab

class TabsReducer : Reducer<TabsState, TabsAction> {
    override fun reduce(state: TabsState, action: TabsAction): TabsState {
        return when (action) {
            TabsAction.OpenCheeses -> state.copy(currentTab = Tab.CheeseList)
            TabsAction.OpenTasks -> state.copy(currentTab = Tab.TasksList)
            TabsAction.OpenReports -> state.copy(currentTab = Tab.ReportList)

            TabsAction.OpenSettings -> TODO()
            else -> state.copy()
        }
    }
}