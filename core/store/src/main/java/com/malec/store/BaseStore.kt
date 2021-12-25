package com.malec.store

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.concurrent.CopyOnWriteArrayList

abstract class BaseStore<State, Action>(
    currentState: State,
    private val reducer: Reducer<State, Action>,
    private val errorHandler: ErrorHandler,
    private val sideEffects: Iterable<SideEffect<State, Action>> = CopyOnWriteArrayList(),
    private val actionSources: Iterable<ActionSource<Action>> = CopyOnWriteArrayList(),
    private val actionHandlers: Iterable<ActionHandler<Action>> = CopyOnWriteArrayList()
) : Store<State, Action> {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _actions = MutableSharedFlow<Action>(0, 1, BufferOverflow.DROP_OLDEST)
    private val actions = _actions.asSharedFlow()

    private val _state = MutableStateFlow(currentState)
    override val state = _state.asStateFlow()

    init {
        scope.launch {
            reduce()
        }
        scope.launch {
            dispatchActionSource()
        }
    }

    override fun dispatchAction(action: Action) {
        scope.launch {
            _actions.emit(action)
        }
    }

    override fun dispose() {
        scope.cancel()
    }

    private suspend fun reduce() {
        actions.onEach { action ->
            val state = reducer.reduce(state.value, action)
            _state.value = state
            dispatchSideEffect(state, action)
            dispatchActionHandler(action)
        }.catch {
            errorHandler.handle(it)
        }.collect()
    }

    private suspend fun dispatchSideEffect(state: State, action: Action) {
        sideEffects.filter { sideEffect ->
            sideEffect.requirement(state, action)
        }.forEach { sideEffect ->
            try {
                _actions.emit(sideEffect(state, action))
            } catch (t: Throwable) {
                errorHandler.handle(t)
                _actions.emit(sideEffect(t))
            }
        }
    }

    private suspend fun dispatchActionSource() {
        actionSources.forEach { actionSource ->
            actionSource()().catch {
                errorHandler.handle(it)
                _actions.emit(actionSource(it))
            }.collectLatest { action ->
                _actions.emit(action)
            }
        }
    }

    private suspend fun dispatchActionHandler(action: Action) {
        actionHandlers.filter { actionHandler ->
            actionHandler.requirements(action)
        }.forEach { actionHandler ->
            try {
                withContext(actionHandler.dispatcher) {
                    actionHandler(action)
                }
            } catch (t: Throwable) {
                errorHandler.handle(t)
            }
        }
    }
}
