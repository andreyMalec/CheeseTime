package com.malec.tasklist.presentation.store.actionhandler

import com.malec.store.ActionHandler
import com.malec.tasklist.dependencies.TaskListOutput
import com.malec.tasklist.presentation.store.TaskListAction

class OpenDetailActionHandler(
    private val taskListOutput: TaskListOutput
) : ActionHandler<TaskListAction>(
    requirement = { action -> action is TaskListAction.OpenDetail },
    handler = { action ->
        action as TaskListAction.OpenDetail
        taskListOutput.openDetail(action.taskId)
    }
)