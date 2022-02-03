package com.malec.domain.usecase

import com.malec.domain.repository.CheeseRepo
import com.malec.interactor.UseCase

class ShareCheesesUseCase(
    private val cheeseRepo: CheeseRepo
) : UseCase<ShareCheesesUseCaseParams, Unit>() {
    override suspend fun build(params: ShareCheesesUseCaseParams) {
        return cheeseRepo.share(params.cheeses)
    }
}