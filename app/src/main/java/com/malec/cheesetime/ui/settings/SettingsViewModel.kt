package com.malec.cheesetime.ui.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.malec.cheesetime.model.Recipe
import com.malec.cheesetime.repo.UserRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val context: Context,
    private val userRepo: UserRepo,
    private val router: Router
) : ViewModel() {
    val recipes = MutableLiveData<List<Recipe>>()

    init {
        updateRecipes()
    }

    private fun updateRecipes() {
        viewModelScope.launch {
            recipes.value = userRepo.getRecipes()
        }
    }

    fun saveSettings() {
        router.exit()
    }

    fun onRemoveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            userRepo.deleteRecipeById(recipe.id)

            updateRecipes()
        }
    }

    fun onSaveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            if (recipe.id == 0L) {
                val nextId = userRepo.getNextRecipeId()
                userRepo.createRecipe(Recipe(nextId, recipe.name, recipe.stages))
            } else
                userRepo.updateRecipe(recipe)

            updateRecipes()
        }
    }
}