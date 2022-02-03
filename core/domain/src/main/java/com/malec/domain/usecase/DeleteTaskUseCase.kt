package com.malec.domain.usecase

import com.malec.domain.repository.TaskRepo
import com.malec.interactor.UseCase

class DeleteTaskUseCase(
    private val taskRepo: TaskRepo
) : UseCase<TaskUseCaseParams, Unit>() {
    override suspend fun build(params: TaskUseCaseParams) {
        return taskRepo.delete(params.task)
    }
}