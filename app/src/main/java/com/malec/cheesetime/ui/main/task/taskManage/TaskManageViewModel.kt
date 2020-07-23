package com.malec.cheesetime.ui.main.task.taskManage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.repo.TaskRepo
import com.malec.cheesetime.util.DateFormatter
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskManageViewModel @Inject constructor(
    private val repo: TaskRepo,
    private val context: Context
) : ViewModel() {
    val cheeseList = MutableLiveData<List<Cheese>>()

    val isFieldsEmptyError = MutableLiveData(false)
    val manageError = MutableLiveData<String>(null)
    val manageResult = MutableLiveData<String>(null)

    val cheese = MutableLiveData<String>("")
    val todo = MutableLiveData<String>("")
    val date = MutableLiveData<String>("")
    val time = MutableLiveData<String>("")
    val comment = MutableLiveData<String>("")
    val task = MutableLiveData<Task>(null)

    init {
        viewModelScope.launch {
            try {
                cheeseList.value = repo.getCheeseList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setTask(newTask: Task) {
        if (newTask.id != 0L)
            task.value = newTask
    }

    fun deleteTask() {
        task.value?.let {
            viewModelScope.launch {
                try {
                    repo.deleteById(it.id)
                    manageResult.value = context.getString(R.string.task_deleted)
                } catch (e: Exception) {
                    setError(e)
                }
            }
        }
    }

    fun checkAndManageTask() = viewModelScope.launch {
        val mTask = task.value
        val mCheese = cheese.value
        val mCheeseId = mCheese?.split(" id: ")?.get(1)
        val mCheeseName = mCheese?.split(" id: ")?.get(0)
        val mTodo = todo.value
        val mDate = date.value
        val mTime = time.value
        val mComment = comment.value

        if (mTodo.isNullOrBlank() || mDate.isNullOrBlank() || mTime.isNullOrBlank()) {
            isFieldsEmptyError.value = true
            return@launch
        }

        val date = DateFormatter.dateFromString(mDate, mTime)

        val task = Task(
            mTask?.id ?: getNextId(),
            mCheeseId?.toLong() ?: 0,
            mCheeseName ?: "",
            mTodo,
            date,
            mComment ?: ""
        )

        try {
            manageResult.value = if (mTask == null) {
                repo.create(task)
                context.getString(R.string.task_created)
            } else {
                repo.update(task)
                context.getString(R.string.task_updated)
            }
        } catch (e: Exception) {
            setError(e)
        }
    }

    private suspend fun getNextId() = try {
        repo.getNextId()
    } catch (e: Exception) {
        e.printStackTrace()
        1L
    }

    private fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        manageError.value = msg.drop(i + 2)
    }
}