package com.malec.taskdetail.usecase

import com.malec.domain.model.Task
import com.malec.domain.repository.TaskRepo
import com.malec.interactor.UseCase

class GetTaskByIdUseCase(
    private val taskRepo: TaskRepo
) : UseCase<GetTaskByIdUseCaseParams, Task?>() {
    override suspend fun build(params: GetTaskByIdUseCaseParams) = taskRepo.getById(params.taskId)
}