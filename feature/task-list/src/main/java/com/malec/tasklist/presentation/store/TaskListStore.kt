package com.malec.tasklist.presentation.store

import com.malec.store.*
import java.util.concurrent.CopyOnWriteArrayList

class TaskListStore(
    currentState: TaskListState,
    reducer: TaskListReducer,
    errorHandler: ErrorHandler,
    sideEffects: List<SideEffect<TaskListState, TaskListAction>> = CopyOnWriteArrayList(),
    bindActionSources: List<BindActionSource<TaskListState, TaskListAction>> = CopyOnWriteArrayList(),
    actionSources: List<ActionSource<TaskListAction>> = CopyOnWriteArrayList(),
    actionHandlers: List<ActionHandler<TaskListState, TaskListAction>> = CopyOnWriteArrayList()
) : BaseStore<TaskListState, TaskListAction>(
    currentState,
    reducer,
    errorHandler,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)