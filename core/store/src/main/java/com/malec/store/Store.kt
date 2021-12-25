package com.malec.store

import kotlinx.coroutines.flow.Flow

interface Store<State, Action> {

    val state: Flow<State>

    fun dispatchAction(action: Action)

    fun dispose()
}