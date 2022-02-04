package com.malec.tasklist.presentation.store.actionhandler

import com.malec.store.ActionHandler
import com.malec.tasklist.dependencies.TaskListOutput
import com.malec.tasklist.presentation.store.TaskListAction
import com.malec.tasklist.presentation.store.TaskListState

class OpenDetailActionHandler(
    private val taskListOutput: TaskListOutput
) : ActionHandler<TaskListState, TaskListAction>(
    requirement = { action -> action is TaskListAction.OpenDetail },
    handler = { _, action ->
        action as TaskListAction.OpenDetail
        taskListOutput.openDetail(action.taskId)
    }
)