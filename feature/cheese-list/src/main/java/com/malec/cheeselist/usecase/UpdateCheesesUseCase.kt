package com.malec.cheeselist.usecase

import com.malec.domain.model.Cheese
import com.malec.domain.repository.CheeseRepo
import com.malec.interactor.UseCase

class UpdateCheesesUseCase(
    private val cheeseRepo: CheeseRepo
) : UseCase<SearchUseCaseParams, List<Cheese>>() {
    override suspend fun build(params: SearchUseCaseParams): List<Cheese> {
        return cheeseRepo.getAllTitleContains(params.filter, params.query).map {
            if (params.selectedIds.contains(it.id))
                it.toggleSelect()
            else
                it
        }
    }
}