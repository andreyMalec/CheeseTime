package com.malec.domain.usecase

import com.malec.domain.model.Recipe
import com.malec.domain.repository.UserRepo
import com.malec.interactor.EmptyParams
import com.malec.interactor.UseCase

class GetRecipesUseCase(
    private val userRepo: UserRepo
) : UseCase<EmptyParams, List<Recipe>>() {
    override suspend fun build(params: EmptyParams): List<Recipe> {
        return userRepo.getRecipes()
    }
}