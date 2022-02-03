package com.malec.cheesedetail.presentation.store

import com.malec.domain.util.DateFormatter
import com.malec.store.Reducer

class CheeseDetailReducer : Reducer<CheeseDetailState, CheeseDetailAction> {
    override fun reduce(state: CheeseDetailState, action: CheeseDetailAction): CheeseDetailState {
        return when (action) {
            is CheeseDetailAction.ShowCheese -> state.copy(
                cheeseId = action.cheese.id,
                cheese = action.cheese,
                isDeleteActive = action.cheese.id != 0L
            )
            is CheeseDetailAction.ShowRecipes -> state.copy(
                recipes = action.recipes
            )
            is CheeseDetailAction.SelectRecipeAtPosition -> state.copy(
                selectedRecipePosition = action.position
            )
            is CheeseDetailAction.SelectRecipe -> state.copy(
                cheese = state.cheese.copy(
                    recipe = action.recipe
                )
            )
            is CheeseDetailAction.SetName -> state.copy(
                cheese = state.cheese.copy(
                    name = action.name
                )
            )
            is CheeseDetailAction.SetDate -> state.copy(
                cheese = state.cheese.copy(
                    date = DateFormatter.dateFromString(action.date)
                )
            )
            is CheeseDetailAction.SetComment -> state.copy(
                cheese = state.cheese.copy(
                    comment = action.comment
                )
            )
            is CheeseDetailAction.SetMilkType -> state.copy(
                cheese = state.cheese.copy(
                    milkType = action.type
                )
            )
            is CheeseDetailAction.SetMilkVolume -> state.copy(
                cheese = state.cheese.copy(
                    milkVolume = action.volume
                )
            )
            is CheeseDetailAction.SetMilkAge -> state.copy(
                cheese = state.cheese.copy(
                    milkAge = action.age
                )
            )
            is CheeseDetailAction.SetComposition -> state.copy(
                cheese = state.cheese.copy(
                    composition = action.composition
                )
            )
            is CheeseDetailAction.SetVolume -> state.copy(
                cheese = state.cheese.copy(
                    volume = action.volume
                )
            )
            is CheeseDetailAction.SetVolumeMax -> state.copy(
                cheese = state.cheese.copy(
                    volumeMax = action.volumeMax
                )
            )
            is CheeseDetailAction.SetBadgeColor -> state.copy(
                cheese = state.cheese.copy(
                    badgeColor = action.color
                )
            )
            is CheeseDetailAction.SetCanSave -> state.copy(
                isSaveActive = action.canSave
            )

            is CheeseDetailAction.Error -> state.copy(
                error = action.throwable
            )

            else -> state.copy()
        }
    }
}