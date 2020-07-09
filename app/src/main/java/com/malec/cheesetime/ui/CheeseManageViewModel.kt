package com.malec.cheesetime.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheeseManageViewModel : ViewModel() {
    val badgeColor = MutableLiveData<Int>()
}