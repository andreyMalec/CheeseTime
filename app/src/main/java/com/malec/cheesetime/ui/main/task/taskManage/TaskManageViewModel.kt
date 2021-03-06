package com.malec.cheesetime.ui.main.task.taskManage

import androidx.lifecycle.MutableLiveData
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.repo.TaskRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.base.BaseViewModel
import com.malec.cheesetime.ui.base.ManageViewModel
import com.malec.cheesetime.util.DateFormatter
import javax.inject.Inject

class TaskManageViewModel @Inject constructor(
    private val repo: TaskRepo,
    private val res: Resources
) : BaseViewModel(), ManageViewModel {

    override val manageError = MutableLiveData<String?>(null)
    override val manageResult = MutableLiveData<String?>(null)
    override val isSaveActive = MutableLiveData(false)
    override val isDeleteActive = MutableLiveData(false)

    val task = MutableLiveData<Task?>(null)
    val date = MutableLiveData("")
    val time = MutableLiveData("")

    val offsetPosition = MutableLiveData(0)

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
                    mCheeseList.add(Cheese.empty().apply {
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
        isSaveActive.value =
            !(task.value?.todo.isNullOrBlank() || date.value.isNullOrBlank() || time.value.isNullOrBlank())
    }

    fun setTask(newTask: Task?) {
        if (task.value == null) {
            task.value = if (newTask != null) {
                isDeleteActive.value = true
                date.value = DateFormatter.simpleFormat(newTask.date)
                time.value = DateFormatter.simpleFormatTime(newTask.date)
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

    fun updateTimeOffsetByPosition(position: Int) {
        offsetPosition.value = position
        val offset = offsetByPosition(position)
        val mTask = task.value
        val time = mTask?.date ?: System.currentTimeMillis()
        date.value = DateFormatter.simpleFormat(time + offset)
        this.time.value = DateFormatter.simpleFormatTime(time + offset)
    }

    private fun offsetByPosition(position: Int): Long {
        return when (position) {
            0 -> DateFormatter.millisecondsInMinute * 10
            1 -> DateFormatter.millisecondsInMinute * 15
            2 -> DateFormatter.millisecondsIn30Min
            3 -> DateFormatter.millisecondsInHour
            4 -> DateFormatter.millisecondsInHour * 3
            5 -> DateFormatter.millisecondsInHour * 5
            6 -> DateFormatter.millisecondsInDay
            else -> 0
        }
    }

    override fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        manageError.value = msg.drop(i + 2)
    }
}