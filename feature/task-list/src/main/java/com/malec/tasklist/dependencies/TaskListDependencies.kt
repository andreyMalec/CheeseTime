package com.malec.tasklist.dependencies

import com.malec.domain.repository.TaskRepo
import com.malec.domain.usecase.DeleteTaskUseCase
import com.malec.presentation.unidirectional.BaseDependencies

interface TaskListDependencies : BaseDependencies {
    fun getTaskListOutput(): TaskListOutput
    fun getTaskRepo(): TaskRepo
    fun getDeleteTaskUseCase(): DeleteTaskUseCase
}