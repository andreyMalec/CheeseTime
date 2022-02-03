package com.malec.cheeselist.dependencies

import com.github.terrakok.cicerone.NavigatorHolder
import com.malec.domain.repository.CheeseRepo
import com.malec.domain.repository.UserRepo
import com.malec.domain.usecase.DeleteCheeseUseCase
import com.malec.domain.usecase.GetRecipesUseCase
import com.malec.domain.usecase.ShareCheesesUseCase
import com.malec.domain.usecase.UpdateCheeseUseCase
import com.malec.presentation.Resources
import com.malec.presentation.unidirectional.BaseDependencies

interface CheeseListDependencies : BaseDependencies {
    fun getNavHolder(): NavigatorHolder
    fun getCheeseRepo(): CheeseRepo
    fun getUserRepo(): UserRepo
    fun getResources(): Resources
    fun getCheeseListOutput(): CheeseListOutput
    fun getGetRecipesUseCase(): GetRecipesUseCase
    fun getUpdateCheeseUseCase(): UpdateCheeseUseCase
    fun getDeleteCheeseByIdUseCase(): DeleteCheeseUseCase
    fun getShareCheesesUseCase(): ShareCheesesUseCase
}