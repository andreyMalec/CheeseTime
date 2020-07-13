package com.malec.cheesetime.ui.main.cheeseList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.repo.CheeseRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CheeseListViewModel @Inject constructor(
    private val repo: CheeseRepo
) : ViewModel(), CheeseAdapter.CheeseAction {
    val cheeseList = MutableLiveData<List<Cheese>>(null)

    init {
        update()
    }

    fun update() {
        repo.getAll({
            cheeseList.value = it
        }, {

        })
    }

    override fun deleteCheese(cheese: Cheese) {

    }

    override fun editCheese(cheese: Cheese) {

    }
}