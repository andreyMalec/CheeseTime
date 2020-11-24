package com.malec.cheesetime.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router,
    private val repo: CheeseRepo,
    private val userRepo: UserRepo
) : BaseViewModel() {
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
        replaceScreen(Screens.taskList())
    }

    fun onCheeseListClick() {
        replaceScreen(Screens.cheeseList())
    }

    fun onReportsClick() {
        replaceScreen(Screens.reports())
    }

    fun onScanClick() {
        router.navigateTo(Screens.scan())
    }

    fun onScanSuccess(result: String) {
        safeRun {
            val cheese = repo.getById(result.toLong())
            router.navigateTo(Screens.cheeseManage(cheese))
        }
    }

    fun onFABClick() {
        when (currentScreen.screenKey) {
            Screens.TASK_LIST -> router.navigateTo(Screens.taskManage())
            Screens.CHEESE_LIST -> router.navigateTo(Screens.cheeseManage())
            Screens.REPORTS -> {
            }
        }
    }

    fun logout() {
        safeRun {
            userRepo.logout()
            router.newRootScreen(Screens.login())
        }
    }

    fun showSettings() {
        router.navigateTo(Screens.settings())
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

    override fun setError(t: Throwable?) {}
}