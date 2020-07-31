package com.malec.cheesetime.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malec.cheesetime.repo.UserRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsViewModel @Inject constructor(
    private val router: Router,
    private val userRepo: UserRepo
) : ViewModel() {
    val recipes = MutableLiveData<List<String>>()
}