package com.malec.domain.usecase

import com.malec.domain.repository.CheeseRepo
import com.malec.interactor.UseCase

class UpdateCheeseUseCase(
    private val cheeseRepo: CheeseRepo
) : UseCase<CheeseUseCaseParams, Unit>() {
    override suspend fun build(params: CheeseUseCaseParams) {
        return cheeseRepo.update(params.cheese)
    }
}