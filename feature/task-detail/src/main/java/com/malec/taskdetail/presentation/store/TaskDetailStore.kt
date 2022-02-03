package com.malec.taskdetail.presentation.store

import com.malec.store.*
import java.util.concurrent.CopyOnWriteArrayList

class TaskDetailStore(
    currentState: TaskDetailState,
    reducer: TaskDetailReducer,
    errorHandler: ErrorHandler,
    sideEffects: List<SideEffect<TaskDetailState, TaskDetailAction>> = CopyOnWriteArrayList(),
    bindActionSources: List<BindActionSource<TaskDetailAction>> = CopyOnWriteArrayList(),
    actionSources: List<ActionSource<TaskDetailAction>> = CopyOnWriteArrayList(),
    actionHandlers: List<ActionHandler<TaskDetailAction>> = CopyOnWriteArrayList()
) : BaseStore<TaskDetailState, TaskDetailAction>(
    currentState,
    reducer,
    errorHandler,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)