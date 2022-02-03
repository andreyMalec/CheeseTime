package com.malec.tabs.presentation.store

import com.malec.store.*
import java.util.concurrent.CopyOnWriteArrayList

class TabsStore(
    currentState: TabsState,
    reducer: TabsReducer,
    errorHandler: ErrorHandler,
    sideEffects: List<SideEffect<TabsState, TabsAction>> = CopyOnWriteArrayList(),
    bindActionSources: List<BindActionSource<TabsAction>> = CopyOnWriteArrayList(),
    actionSources: List<ActionSource<TabsAction>> = CopyOnWriteArrayList(),
    actionHandlers: List<ActionHandler<TabsAction>> = CopyOnWriteArrayList()
) : BaseStore<TabsState, TabsAction>(
    currentState,
    reducer,
    errorHandler,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)