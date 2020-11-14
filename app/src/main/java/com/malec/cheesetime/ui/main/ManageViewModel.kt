package com.malec.cheesetime.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malec.cheesetime.ui.BaseViewModel

abstract class ManageViewModel : BaseViewModel() {
    val manageError = MutableLiveData<String>(null)
    val manageResult = MutableLiveData<String>(null)

    protected val _isSaveActive = MutableLiveData(false)
    val isSaveActive: LiveData<Boolean>
        get() = _isSaveActive

    protected val _isDeleteActive = MutableLiveData(false)
    val isDeleteActive: LiveData<Boolean>
        get() = _isDeleteActive

    override fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        manageError.value = msg.drop(i + 2)
    }

    abstract fun checkCanSave()
}