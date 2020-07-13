package com.malec.cheesetime.ui.main.cheeseList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malec.cheesetime.model.Cheese
import javax.inject.Inject

class CheeseListViewModel @Inject constructor(
    private val context: Context
) : ViewModel(), CheeseAdapter.CheeseAction {
    val cheeseList = MutableLiveData<List<Cheese>>(null)

    override fun deleteCheese(cheese: Cheese) {

    }

    override fun editCheese(cheese: Cheese) {

    }
}