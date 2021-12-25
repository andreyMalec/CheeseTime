package com.malec.store

interface Reducer<State, Action> {
    fun reduce(state: State, action: Action): State
}