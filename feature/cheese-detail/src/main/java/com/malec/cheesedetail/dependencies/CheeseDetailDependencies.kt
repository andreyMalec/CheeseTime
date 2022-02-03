package com.malec.cheesedetail.dependencies

import com.malec.domain.repository.CheeseRepo
import com.malec.domain.usecase.GetRecipesUseCase
import com.malec.presentation.unidirectional.BaseDependencies

interface CheeseDetailDependencies : BaseDependencies {
    fun getCheeseDetailOutput(): CheeseDetailOutput
    fun getCheeseRepo(): CheeseRepo
    fun getGetRecipesUseCase(): GetRecipesUseCase
}