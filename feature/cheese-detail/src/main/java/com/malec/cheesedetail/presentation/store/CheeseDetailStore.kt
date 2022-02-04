package com.malec.cheesedetail.presentation.store

import com.malec.store.*
import java.util.concurrent.CopyOnWriteArrayList

class CheeseDetailStore(
    currentState: CheeseDetailState,
    reducer: CheeseDetailReducer,
    errorHandler: ErrorHandler,
    sideEffects: List<SideEffect<CheeseDetailState, CheeseDetailAction>> = CopyOnWriteArrayList(),
    bindActionSources: List<BindActionSource<CheeseDetailState, CheeseDetailAction>> = CopyOnWriteArrayList(),
    actionSources: List<ActionSource<CheeseDetailAction>> = CopyOnWriteArrayList(),
    actionHandlers: List<ActionHandler<CheeseDetailState, CheeseDetailAction>> = CopyOnWriteArrayList()
) : BaseStore<CheeseDetailState, CheeseDetailAction>(
    currentState,
    reducer,
    errorHandler,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)