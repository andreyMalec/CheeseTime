package com.malec.cheesetime.ui.cheeseManage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class CheeseManageViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {
    val badgeColor = MutableLiveData<Int>()
}