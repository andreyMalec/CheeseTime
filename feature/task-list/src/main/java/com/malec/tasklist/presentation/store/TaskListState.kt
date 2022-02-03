package com.malec.tasklist.presentation.store

import com.malec.domain.model.Task

data class TaskListState(
    val tasks: List<Task> = listOf(),
    val isLoading: Boolean = false,
    val isNeedToUpdate: Boolean = false,
    val error: Throwable? = null
)
