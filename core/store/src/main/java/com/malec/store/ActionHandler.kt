package com.malec.store

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class ActionHandler<Action>(
    val requirement: (Action) -> Boolean,
    private val handler: suspend (Action) -> Unit,
    val dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    suspend operator fun invoke(action: Action) = handler(action)
}