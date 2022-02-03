package com.malec.taskdetail.presentation.store.actionhandler

import com.malec.store.ActionHandler
import com.malec.taskdetail.dependencies.TaskDetailOutput
import com.malec.taskdetail.presentation.store.TaskDetailAction

class BackActionHandler(
    private val taskDetailOutput: TaskDetailOutput
) : ActionHandler<TaskDetailAction>(
    requirement = { action -> action is TaskDetailAction.Back },
    handler = {
        taskDetailOutput.exit()
    }
)