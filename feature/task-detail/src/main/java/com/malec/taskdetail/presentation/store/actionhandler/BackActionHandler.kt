package com.malec.taskdetail.presentation.store.actionhandler

import com.malec.store.ActionHandler
import com.malec.taskdetail.dependencies.TaskDetailOutput
import com.malec.taskdetail.presentation.store.TaskDetailAction
import com.malec.taskdetail.presentation.store.TaskDetailState

class BackActionHandler(
    private val taskDetailOutput: TaskDetailOutput
) : ActionHandler<TaskDetailState, TaskDetailAction>(
    requirement = { action -> action is TaskDetailAction.Back },
    handler = { _, _ ->
        taskDetailOutput.exit()
    }
)