package com.malec.cheesetime.coordinator

import com.github.terrakok.cicerone.Router
import com.malec.cheeselist.CheeseListScreen
import com.malec.reportlist.ReportListScreen
import com.malec.signin.SignInScreen
import com.malec.tabs.dependencies.TabsOutput
import com.malec.tasklist.TaskListScreen

class TabsCoordinator(
    private val router: Router
) : TabsOutput {
    override fun openCheeseList() {
        router.replaceScreen(CheeseListScreen)
    }

    override fun openTaskList() {
        router.replaceScreen(TaskListScreen)
    }

    override fun openReportList() {
        router.replaceScreen(ReportListScreen)
    }

    override fun openSettings() {

    }

    override fun logout() {
        router.newRootScreen(SignInScreen)
    }

    override fun exit() {
        router.exit()
    }
}