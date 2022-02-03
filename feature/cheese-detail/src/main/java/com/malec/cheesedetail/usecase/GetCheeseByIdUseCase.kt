package com.malec.cheesedetail.usecase

import com.malec.domain.model.Cheese
import com.malec.domain.repository.CheeseRepo
import com.malec.interactor.UseCase

class GetCheeseByIdUseCase(
    private val cheeseRepo: CheeseRepo
) : UseCase<GetCheeseByIdUseCaseParams, Cheese?>() {
    override suspend fun build(params: GetCheeseByIdUseCaseParams) =
        cheeseRepo.getById(params.cheeseId)
}