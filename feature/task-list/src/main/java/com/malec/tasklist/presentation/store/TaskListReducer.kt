package com.malec.tasklist.presentation.store

import com.malec.store.Reducer

class TaskListReducer : Reducer<TaskListState, TaskListAction> {
    override fun reduce(state: TaskListState, action: TaskListAction): TaskListState {
        return when (action) {
            is TaskListAction.UpdateTasks -> state.copy(
                isLoading = true
            )
            is TaskListAction.ShowTasks -> state.copy(
                tasks = action.tasks,
                isLoading = false,
                isNeedToUpdate = false
            )

            is TaskListAction.UpdateTime -> state.copy(
                isNeedToUpdate = true
            )

            is TaskListAction.Error -> state.copy(
                error = action.throwable
            )

            else -> state.copy()
        }
    }
}