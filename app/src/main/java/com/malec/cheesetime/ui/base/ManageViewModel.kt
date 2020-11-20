package com.malec.cheesetime.ui.base

import androidx.lifecycle.MutableLiveData

interface ManageViewModel {
    val manageError: MutableLiveData<String>
    val manageResult: MutableLiveData<String>

    val isSaveActive: MutableLiveData<Boolean>
    val isDeleteActive: MutableLiveData<Boolean>

    fun checkCanSave()
}