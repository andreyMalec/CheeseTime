package com.malec.cheesedetail.presentation.store.sideeffect

import com.malec.cheesedetail.presentation.store.CheeseDetailAction
import com.malec.cheesedetail.presentation.store.CheeseDetailState
import com.malec.domain.model.Recipe
import com.malec.domain.usecase.GetRecipesUseCase
import com.malec.interactor.EmptyParams
import com.malec.store.SideEffect

class ShowCheeseSideEffect(
    useCase: GetRecipesUseCase
) : SideEffect<CheeseDetailState, CheeseDetailAction>(
    requirement = { _, action -> action is CheeseDetailAction.ShowCheese },
    effect = { _, action ->
        action as CheeseDetailAction.ShowCheese
        val recipes = useCase.build(EmptyParams).toMutableList()
        if (action.cheese.id == 0L)
            CheeseDetailAction.ShowRecipes(recipes)
        else {
            val currentRecipe = recipes.find {
                it.name == action.cheese.recipe
            } ?: Recipe(-1, action.cheese.recipe, action.cheese.stages)
            recipes.remove(currentRecipe)
            recipes.add(0, currentRecipe)
            CheeseDetailAction.ShowRecipes(recipes)
        }
    },
    error = { CheeseDetailAction.Error(it) }
)