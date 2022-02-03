package com.malec.tasklist.usecase

import com.malec.domain.model.Task
import com.malec.domain.repository.TaskRepo
import com.malec.interactor.EmptyParams
import com.malec.interactor.UseCase

class GetTasksUseCase(
    private val taskRepo: TaskRepo
) : UseCase<EmptyParams, List<Task>>() {
    override suspend fun build(params: EmptyParams) = taskRepo.getAll()
}