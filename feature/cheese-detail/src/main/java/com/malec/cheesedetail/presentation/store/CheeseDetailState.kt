package com.malec.cheesedetail.presentation.store

import com.malec.domain.model.Cheese
import com.malec.domain.model.Recipe

data class CheeseDetailState(
    val cheeseId: Long = 0L,
    val cheese: Cheese = Cheese.empty(),
    val recipes: List<Recipe> = listOf(),
    val isUpdateRecipes: Boolean = true,
    val selectedRecipePosition: Int = -1,
    val isSaveActive: Boolean = false,
    val isDeleteActive: Boolean = false,
    val error: Throwable? = null
)