package com.malec.cheesetime.ui.main.task.taskManage

import androidx.lifecycle.ViewModel
import com.malec.cheesetime.repo.TaskRepo
import javax.inject.Inject

class TaskManageViewModel @Inject constructor(
    private val repo: TaskRepo
) : ViewModel()