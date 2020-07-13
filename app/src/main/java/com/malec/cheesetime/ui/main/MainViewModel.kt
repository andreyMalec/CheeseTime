package com.malec.cheesetime.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.ui.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router
) : ViewModel() {
    private var pressAgain = true
    private val pressAgainAwait = 2000L

    private val _showPressAgain = MutableLiveData(false)
    val showPressAgain: LiveData<Boolean>
        get() = _showPressAgain

    init {
        onTaskListClick()
    }

    fun onTaskListClick() {
        router.replaceScreen(Screens.TaskListScreen)
    }

    fun onCheeseListClick() {
        router.replaceScreen(Screens.CheeseListScreen)
    }

    fun onReportsClick() {
        router.replaceScreen(Screens.ReportsScreen)
    }

    fun onScanClick() {

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