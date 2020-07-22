package com.malec.cheesetime.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.integration.android.IntentIntegrator
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val router: Router,
    private val repo: CheeseRepo
) : ViewModel() {
    private var pressAgain = true
    private val pressAgainAwait = 2000L

    private val _showPressAgain = MutableLiveData(false)
    val showPressAgain: LiveData<Boolean>
        get() = _showPressAgain

    private lateinit var currentScreen: Screen

    init {
        onTaskListClick()
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
            setPrompt("Scan a barcode")
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

    private fun replaceScreen(screen: Screen) {
        currentScreen = screen
        router.replaceScreen(currentScreen)
    }

    fun onBackPressed() {
        if (pressAgain) {
            pressAgain = false
            viewModelScope.launch {
                delay(pressAgainAwait)
                pressAgain = true
            }
            _showPressAgain.value = true
            return
        }
        router.exit()
    }
}