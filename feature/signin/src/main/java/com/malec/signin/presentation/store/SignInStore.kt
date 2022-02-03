package com.malec.signin.presentation.store

import com.malec.store.*
import java.util.concurrent.CopyOnWriteArrayList

class SignInStore(
    currentState: SignInState,
    reducer: SignInReducer,
    errorHandler: ErrorHandler,
    sideEffects: List<SideEffect<SignInState, SignInAction>> = CopyOnWriteArrayList(),
    bindActionSources: List<BindActionSource<SignInAction>> = CopyOnWriteArrayList(),
    actionSources: List<ActionSource<SignInAction>> = CopyOnWriteArrayList(),
    actionHandlers: List<ActionHandler<SignInAction>> = CopyOnWriteArrayList()
) : BaseStore<SignInState, SignInAction>(
    currentState,
    reducer,
    errorHandler,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)