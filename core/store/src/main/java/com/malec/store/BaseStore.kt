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
    private val bindActionSources: Iterable<BindActionSource<Action>> = CopyOnWriteArrayList(),
    private val actionSources: Iterable<ActionSource<Action>> = CopyOnWriteArrayList(),
    private val actionHandlers: Iterable<ActionHandler<Action>> = CopyOnWriteArrayList()
) : Store<State, Action> {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _actions = MutableSharedFlow<Action>(0, 10, BufferOverflow.DROP_OLDEST)
    private val actions = _actions.asSharedFlow()

    private val _state = MutableSharedFlow<State>(1, 10, BufferOverflow.DROP_OLDEST)
    override val state = _state.asSharedFlow() as Flow<State>

    private var _currentState: State = currentState
    override val currentState: State
        get() = _currentState

    init {
        scope.launch(Dispatchers.IO) {
            _state.emit(currentState)
            handleActions()
        }
        scope.launch(Dispatchers.IO) {
            dispatchActionSource()
        }
    }

    override fun dispatchAction(action: Action) {
        scope.launch(Dispatchers.IO) {
            _actions.emit(action)
        }
    }

    override fun dispose() {
        scope.cancel()
    }

    private suspend fun handleActions() {
        actions.collect { action ->
            val state = reducer.reduce(currentState, action)
            _state.emit(state)
            _currentState = state
            dispatchSideEffect(state, action)
            dispatchActionHandler(action)
            dispatchBindActionSource(action)
        }
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
        actionSources.map { actionSource ->
            try {
                actionSource()
            } catch (t: Throwable) {
                errorHandler.handle(t)
                flowOf(actionSource(t))
            }
        }.merge().collect {
            _actions.emit(it)
        }
    }

    private suspend fun dispatchBindActionSource(action: Action) {
        bindActionSources.filter { bindActionSource ->
            bindActionSource.requirement(action)
        }.map { bindActionSource ->
            try {
                bindActionSource(action)
            } catch (t: Throwable) {
                errorHandler.handle(t)
                flowOf(bindActionSource(t))
            }
        }.merge().collect {
            _actions.emit(it)
        }
    }

    private suspend fun dispatchActionHandler(action: Action) {
        actionHandlers.filter { actionHandler ->
            actionHandler.requirement(action)
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
