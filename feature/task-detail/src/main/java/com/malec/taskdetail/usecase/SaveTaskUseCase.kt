package com.malec.taskdetail.usecase

import com.malec.domain.repository.TaskRepo
import com.malec.domain.usecase.TaskUseCaseParams
import com.malec.interactor.UseCase

class SaveTaskUseCase(
    private val taskRepo: TaskRepo
) : UseCase<TaskUseCaseParams, Unit>() {
    override suspend fun build(params: TaskUseCaseParams) = taskRepo.save(params.task)
}