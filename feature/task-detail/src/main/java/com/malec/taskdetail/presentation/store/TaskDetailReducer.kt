package com.malec.taskdetail.presentation.store

import com.malec.domain.util.DateFormatter
import com.malec.store.Reducer

class TaskDetailReducer : Reducer<TaskDetailState, TaskDetailAction> {
    override fun reduce(state: TaskDetailState, action: TaskDetailAction): TaskDetailState {
        return when (action) {
            is TaskDetailAction.ShowTask -> {
                val s = state.copy(
                    taskId = action.task.id,
                    task = action.task,
                    isDeleteActive = action.task.id != 0L
                )
                return if (action.task.id == 0L)
                    s
                else {
                    s.copy(
                        date = DateFormatter.simpleFormat(action.task.date),
                        time = DateFormatter.simpleFormatTime(action.task.date)
                    )
                }
            }
            is TaskDetailAction.ShowCheeses -> state.copy(
                cheeses = action.cheeses,
                isUpdateCheeses = false
            )
            is TaskDetailAction.SelectCheeseAtPosition -> state.copy(
                selectedCheesePosition = action.position
            )
            is TaskDetailAction.SelectCheese -> state.copy(
                task = state.task.copy(
                    cheeseId = action.cheese.first,
                    cheeseName = action.cheese.second
                )
            )
            is TaskDetailAction.SetTodo -> state.copy(
                task = state.task.copy(
                    todo = action.todo
                )
            )
            is TaskDetailAction.SetComment -> state.copy(
                task = state.task.copy(
                    comment = action.comment
                )
            )
            is TaskDetailAction.SetDateTime -> state.copy(
                date = DateFormatter.simpleFormat(action.dateTime),
                time = DateFormatter.simpleFormatTime(action.dateTime)
            )
            is TaskDetailAction.SetDate -> state.copy(
                date = action.date,
                selectedTimePosition = -1,
                task = state.task.copy(
                    date = DateFormatter.dateTimeFromString(action.date, state.time)
                )
            )
            is TaskDetailAction.SetTime -> state.copy(
                time = action.time,
                selectedTimePosition = -1,
                task = state.task.copy(
                    date = DateFormatter.dateTimeFromString(state.date, action.time)
                )
            )
            is TaskDetailAction.SelectTimeAtPosition -> state.copy(
                selectedTimePosition = action.position
            )
            is TaskDetailAction.SaveTask -> state.copy(
                task = state.task.copy(
                    date = DateFormatter.dateTimeFromString(state.date, state.time)
                )
            )
            is TaskDetailAction.SetCanSave -> state.copy(
                isSaveActive = action.canSave
            )

            is TaskDetailAction.Error -> state.copy(
                error = action.throwable
            )

            else -> state.copy()
        }
    }
}