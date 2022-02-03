package com.malec.store

import kotlinx.coroutines.flow.Flow

open class BindActionSource<Action>(
    val requirement: (Action) -> Boolean,
    private val source: suspend (Action) -> Flow<Action>,
    private val error: (Throwable) -> Action = { t: Throwable -> throw t }
) {
    suspend operator fun invoke(action: Action) = source(action)

    operator fun invoke(throwable: Throwable) = error(throwable)
}