package com.malec.domain.usecase

import com.malec.domain.model.Task
import com.malec.interactor.UseCaseParams

data class TaskUseCaseParams(
    val task: Task
) : UseCaseParams()