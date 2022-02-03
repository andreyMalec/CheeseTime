package com.malec.tabs.presentation.store.actionhandler

import com.malec.store.ActionHandler
import com.malec.tabs.dependencies.TabsOutput
import com.malec.tabs.presentation.store.TabsAction

class OpenTabActionHandler(
    private val tabsOutput: TabsOutput
) : ActionHandler<TabsAction>(
    requirement = { action ->
        action is TabsAction.OpenTasks || action is TabsAction.OpenCheeses ||
                action is TabsAction.OpenReports
    },
    handler = { action ->
        when (action) {
            is TabsAction.OpenTasks -> tabsOutput.openTaskList()
            is TabsAction.OpenCheeses -> tabsOutput.openCheeseList()
            is TabsAction.OpenReports -> tabsOutput.openReportList()
            else -> {}
        }
    }
)