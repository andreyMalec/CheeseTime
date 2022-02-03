package com.malec.taskdetail.usecase

import com.malec.interactor.UseCaseParams

data class GetTaskByIdUseCaseParams(
    val taskId: Long
) : UseCaseParams()