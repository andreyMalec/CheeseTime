package com.malec.taskdetail.presentation.store

import com.malec.domain.model.Task

data class TaskDetailState(
    val taskId: Long = 0,
    val task: Task = Task.empty(),
    val cheeses: List<String> = listOf(),
    val selectedCheesePosition: Int = 0,
    val date: String = "",
    val time: String = "",
    val selectedTimePosition: Int = -1,
    val isSaveActive: Boolean = false,
    val isDeleteActive: Boolean = false,
    val isUpdateCheeses: Boolean = true,
    val error: Throwable? = null
)
