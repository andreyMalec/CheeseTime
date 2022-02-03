package com.malec.taskdetail.usecase

import com.malec.domain.model.CheeseFilter
import com.malec.domain.repository.CheeseRepo
import com.malec.interactor.EmptyParams
import com.malec.interactor.UseCase

class GetCheesesUseCase(
    private val cheeseRepo: CheeseRepo
) : UseCase<EmptyParams, List<String>>() {
    override suspend fun build(params: EmptyParams) =
        cheeseRepo.getAllFiltered(CheeseFilter.empty).map {
            "${it.name} id: ${it.id}"
        }
}