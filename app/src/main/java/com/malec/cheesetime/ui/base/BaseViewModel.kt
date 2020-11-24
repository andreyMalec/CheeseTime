package com.malec.cheesetime.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected abstract fun setError(t: Throwable?)

    protected inline fun safeRun(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                Log.e("BaseViewModel.safeRun", Log.getStackTraceString(e), e)
                setError(e)
            }
        }
    }
}