package com.malec.cheesetime.coordinator

import com.github.terrakok.cicerone.Router
import com.malec.taskdetail.TaskDetailScreen
import com.malec.tasklist.dependencies.TaskListOutput

class TaskListCoordinator(
    private val router: Router
) : TaskListOutput {
    override fun openDetail(id: Long) {
        router.navigateTo(TaskDetailScreen(id))
    }

    override fun onClickExit() {
        router.exit()
    }
}