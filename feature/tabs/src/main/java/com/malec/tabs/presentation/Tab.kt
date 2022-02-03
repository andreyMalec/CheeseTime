package com.malec.tabs.presentation

sealed class Tab {
    abstract val position: Int

    object TasksList : Tab() {
        override val position = 0
    }

    object CheeseList : Tab() {
        override val position = 1
    }

    object ReportList : Tab() {
        override val position = 2
    }

    object None : Tab() {
        override val position = -1
    }
}
