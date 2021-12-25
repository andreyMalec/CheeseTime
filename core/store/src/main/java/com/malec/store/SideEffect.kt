package com.malec.store

open class SideEffect<State, Action>(
    val requirement: (State, Action) -> Boolean,
    private val effect: suspend (State, Action) -> Action,
    private val error: (Throwable) -> Action
) {
    suspend operator fun invoke(state: State, action: Action) = effect(state, action)

    operator fun invoke(throwable: Throwable) = error(throwable)
}