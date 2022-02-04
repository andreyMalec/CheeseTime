package com.malec.main.presentation.store

import com.malec.store.ActionHandler
import com.malec.store.BaseStore
import com.malec.store.ErrorHandler
import java.util.concurrent.CopyOnWriteArrayList

class MainStore(
    currentState: MainState,
    mainReducer: MainReducer,
    errorHandler: ErrorHandler,
    actionHandlers: Iterable<ActionHandler<MainState, MainAction>> = CopyOnWriteArrayList()
) : BaseStore<MainState, MainAction>(
    currentState,
    mainReducer,
    errorHandler,
    actionHandlers = actionHandlers
)