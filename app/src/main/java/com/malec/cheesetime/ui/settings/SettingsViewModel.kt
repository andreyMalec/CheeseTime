package com.malec.cheesetime.ui.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.R
import com.malec.cheesetime.repo.UserRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsViewModel @Inject constructor(
    private val context: Context,
    private val userRepo: UserRepo
) : ViewModel() {
    val recipes = MutableLiveData<List<String>>()

    init {
        viewModelScope.launch {
            recipes.value = userRepo.getRecipes().takeIf { it.isNotEmpty() && it[0].isNotBlank() }
                ?: context.resources.getStringArray(R.array.recipes).toList()
        }
    }

    fun saveSettings() {
        recipes.value?.let {
            viewModelScope.launch {
                userRepo.setRecipes(it)
            }
        }
    }
}