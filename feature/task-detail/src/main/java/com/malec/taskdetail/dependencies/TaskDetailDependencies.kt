package com.malec.taskdetail.dependencies

import com.malec.domain.repository.CheeseRepo
import com.malec.domain.repository.TaskRepo
import com.malec.domain.usecase.DeleteTaskUseCase
import com.malec.presentation.unidirectional.BaseDependencies

interface TaskDetailDependencies : BaseDependencies {
    fun getTaskRepo(): TaskRepo
    fun getCheeseRepo(): CheeseRepo
    fun getTaskDetailOutput(): TaskDetailOutput
    fun getDeleteTaskUseCase(): DeleteTaskUseCase
}