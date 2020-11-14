package com.malec.cheesetime.ui.main.task.taskManage

import androidx.lifecycle.MutableLiveData
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.repo.TaskRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.main.ManageViewModel
import com.malec.cheesetime.util.CheeseCreator
import com.malec.cheesetime.util.DateFormatter
import javax.inject.Inject

class TaskManageViewModel @Inject constructor(
    private val repo: TaskRepo,
    private val res: Resources
) : ManageViewModel() {
    val task = MutableLiveData<Task>(null)
    val date = MutableLiveData("")
    val time = MutableLiveData("")

    val cheeseList = MutableLiveData<MutableList<String>>()
    val cheesePosition = MutableLiveData(-1)

    private val mCheeseList = mutableListOf<Cheese>()

    init {
        safeRun {
            mCheeseList.addAll(repo.getCheeseList())
            val index = mCheeseList.indexOfFirst { it.id == task.value?.cheeseId }
            if (index == -1) {
                val id = task.value?.cheeseId
                val name = task.value?.cheeseName
                if (id != null && id != 0L && !name.isNullOrBlank())
                    mCheeseList.add(CheeseCreator.empty().apply {
                        this.id = task.value?.cheeseId ?: 0
                        this.name = task.value?.cheeseName ?: ""
                    })
            }

            cheeseList.value = mCheeseList.map { it.name + " id: " + it.id }.toMutableList()

            cheesePosition.value = mCheeseList.indexOfFirst { it.id == task.value?.cheeseId }
            selectCheeseAt(cheesePosition.value ?: -1)
        }
    }

    override fun checkCanSave() {
        _isSaveActive.value =
            !(task.value?.todo.isNullOrBlank() || date.value.isNullOrBlank() || time.value.isNullOrBlank())
    }

    fun setTask(newTask: Task?) {
        if (task.value == null) {
            task.value = if (newTask != null) {
                _isDeleteActive.value = true
                date.value = DateFormatter.simpleFormat(newTask.date)
                time.value =
                    DateFormatter.simpleFormatTime(newTask.date % DateFormatter.millisecondsInDay)
                newTask
            } else
                Task.empty()
        }
        checkCanSave()
    }

    fun selectCheeseAt(position: Int) {
        if (position < 0 || position >= mCheeseList.size)
            return

        cheesePosition.value = position

        val cheese = mCheeseList[position]
        task.value?.cheeseName = cheese.name
        task.value?.cheeseId = cheese.id
    }

    fun deleteTask() {
        task.value?.let {
            safeRun {
                repo.deleteById(it.id)
                manageResult.value = res.stringTaskDeleted()
            }
        }
    }

    fun checkAndManageTask() {
        val mTask = task.value
        val mDate = date.value
        val mTime = time.value

        val date = DateFormatter.dateTimeFromString(mDate, mTime)

        safeRun {
            val task = Task(
                mTask?.id.takeIf { it != 0L } ?: repo.getNextId(),
                mTask?.cheeseId ?: 0,
                mTask?.cheeseName ?: "",
                mTask?.todo ?: "",
                date,
                mTask?.comment ?: ""
            )

            manageResult.value = if (mTask?.id == 0L) {
                repo.create(task)
                res.stringTaskCreated()
            } else {
                repo.update(task)
                res.stringTaskUpdated()
            }
        }
    }
}