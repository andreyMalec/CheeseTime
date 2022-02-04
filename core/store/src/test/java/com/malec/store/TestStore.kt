package com.malec.store

import java.util.concurrent.CopyOnWriteArrayList

class TestStore(
    currentState: TestState,
    reducer: TestReducer,
    errorHandler: ErrorHandler,
    actionHandlers: Iterable<ActionHandler<TestState, TestAction>> = CopyOnWriteArrayList(),
    actionSources: Iterable<ActionSource<TestAction>> = CopyOnWriteArrayList(),
    bindActionSources: Iterable<BindActionSource<TestState, TestAction>> = CopyOnWriteArrayList(),
    sideEffects: Iterable<SideEffect<TestState, TestAction>> = CopyOnWriteArrayList()
) : BaseStore<TestState, TestAction>(
    currentState,
    reducer,
    errorHandler,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)