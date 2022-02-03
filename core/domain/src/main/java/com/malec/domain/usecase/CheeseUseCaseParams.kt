package com.malec.domain.usecase

import com.malec.domain.model.Cheese
import com.malec.interactor.UseCaseParams

data class CheeseUseCaseParams(
    val cheese: Cheese
) : UseCaseParams()