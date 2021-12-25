package com.malec.store

import kotlinx.coroutines.flow.Flow

open class ActionSource<Action>(
    private val source: () -> Flow<Action>,
    private val error: (Throwable) -> Action = { t: Throwable -> throw t }
) {
    operator fun invoke() = source

    operator fun invoke(throwable: Throwable) = error(throwable)
}