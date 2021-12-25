package com.malec.store

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class ActionHandler<Action>(
    val requirements: (Action) -> Boolean,
    private val handler: (Action) -> Unit,
    val dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    operator fun invoke(action: Action) = handler(action)
}