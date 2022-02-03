package com.malec.cheeselist.presentation.store

import com.malec.store.*
import java.util.concurrent.CopyOnWriteArrayList

class CheeseListStore(
    currentState: CheeseListState,
    reducer: CheeseListReducer,
    errorHandler: ErrorHandler,
    sideEffects: List<SideEffect<CheeseListState, CheeseListAction>> = CopyOnWriteArrayList(),
    bindActionSources: List<BindActionSource<CheeseListAction>> = CopyOnWriteArrayList(),
    actionSources: List<ActionSource<CheeseListAction>> = CopyOnWriteArrayList(),
    actionHandlers: List<ActionHandler<CheeseListAction>> = CopyOnWriteArrayList()
) : BaseStore<CheeseListState, CheeseListAction>(
    currentState,
    reducer,
    errorHandler,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)