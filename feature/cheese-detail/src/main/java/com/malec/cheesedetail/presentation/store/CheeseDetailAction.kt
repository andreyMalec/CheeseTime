package com.malec.cheesedetail.presentation.store

import com.malec.domain.model.Cheese
import com.malec.domain.model.Recipe

sealed class CheeseDetailAction {
    object Back : CheeseDetailAction()

    data class LoadCheese(val cheeseId: Long) : CheeseDetailAction()
    data class ShowCheese(val cheese: Cheese) : CheeseDetailAction()

    data class ShowRecipes(val recipes: List<Recipe>) : CheeseDetailAction()

    data class SelectRecipeAtPosition(val position: Int) : CheeseDetailAction()
    data class SelectRecipe(val recipe: String) : CheeseDetailAction()
    data class SetName(val name: String) : CheeseDetailAction()
    data class SetDate(val date: String) : CheeseDetailAction()
    data class SetComment(val comment: String) : CheeseDetailAction()
    data class SetMilkType(val type: String) : CheeseDetailAction()
    data class SetMilkVolume(val volume: String) : CheeseDetailAction()
    data class SetMilkAge(val age: String) : CheeseDetailAction()
    data class SetComposition(val composition: String) : CheeseDetailAction()
    data class SetVolume(val volume: String) : CheeseDetailAction()
    data class SetVolumeMax(val volumeMax: String) : CheeseDetailAction()
    data class SetBadgeColor(val color: Int) : CheeseDetailAction()

    data class SetCanSave(val canSave: Boolean) : CheeseDetailAction()

    object AddStage : CheeseDetailAction()

    object DeleteCheese : CheeseDetailAction()

    object SaveCheese : CheeseDetailAction()

    data class Error(val throwable: Throwable) : CheeseDetailAction()
}