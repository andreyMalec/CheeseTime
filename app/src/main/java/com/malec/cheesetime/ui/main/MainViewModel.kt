package com.malec.cheesetime.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.integration.android.IntentIntegrator
import com.malec.cheesetime.R
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.ui.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router,
    private val repo: CheeseRepo,
    private val userRepo: UserRepo
) : ViewModel() {
    private var pressAgain = true
    private val pressAgainAwait = 2000L

    private val _showPressAgain = MutableLiveData(false)
    val showPressAgain: LiveData<Boolean>
        get() = _showPressAgain

    private val _userLogin = MutableLiveData("")
    val userLogin: LiveData<String>
        get() = _userLogin

    private lateinit var currentScreen: Screen

    init {
        onTaskListClick()

        _userLogin.value = userRepo.getUserLogin()
    }

    fun onTaskListClick() {
        replaceScreen(Screens.TaskListScreen)
    }

    fun onCheeseListClick() {
        replaceScreen(Screens.CheeseListScreen)
    }

    fun onReportsClick() {
        replaceScreen(Screens.ReportsScreen)
    }

    fun onScanClick(activity: MainActivity) {
        IntentIntegrator(activity).apply {
            setOrientationLocked(false)
            setPrompt(activity.getString(R.string.scan_code))
        }.initiateScan()
    }

    fun onScanSuccess(result: String) {
        viewModelScope.launch {
            val cheese = repo.getById(result.toLong())
            router.navigateTo(Screens.CheeseManageScreen(cheese))
        }
    }

    fun onFABClick() {
        when (currentScreen) {
            Screens.TaskListScreen -> {
                router.navigateTo(Screens.TaskManageScreen())
            }
            Screens.CheeseListScreen -> {
                router.navigateTo(Screens.CheeseManageScreen())
            }
            Screens.ReportsScreen -> {
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
            router.newRootScreen(Screens.LoginScreen)
        }
    }

    fun showSettings() {
        router.navigateTo(Screens.SettingsScreen)
    }

    private fun replaceScreen(screen: Screen) {
        currentScreen = screen
        router.replaceScreen(currentScreen)
    }

    fun onBackPressed() {
        if (pressAgain) {
            pressAgain = false

            _showPressAgain.value = true
            delay(pressAgainAwait) {
                pressAgain = true
            }
        } else
            router.exit()
    }

    private fun delay(timeInMillis: Long, block: () -> Unit) = viewModelScope.launch {
        delay(timeInMillis)
        block()
    }
}