package com.malec.domain.usecase

import com.malec.domain.model.Cheese
import com.malec.interactor.UseCaseParams

data class ShareCheesesUseCaseParams(
    val cheeses: List<Cheese>
) : UseCaseParams()