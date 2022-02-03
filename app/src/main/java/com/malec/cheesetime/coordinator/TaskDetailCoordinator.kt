package com.malec.cheesetime.coordinator

import com.github.terrakok.cicerone.Router
import com.malec.taskdetail.dependencies.TaskDetailOutput

class TaskDetailCoordinator(
    private val router: Router
) : TaskDetailOutput {
    override fun exit() {
        router.exit()
    }
}