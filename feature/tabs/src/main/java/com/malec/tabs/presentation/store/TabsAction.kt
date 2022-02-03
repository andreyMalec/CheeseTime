package com.malec.tabs.presentation.store

sealed class TabsAction {
    object Ignore : TabsAction()

    object OpenCheeses : TabsAction()
    object OpenTasks : TabsAction()
    object OpenReports : TabsAction()

    object OpenSettings : TabsAction()

    object Logout : TabsAction()

    data class Error(val throwable: Throwable) : TabsAction()
}