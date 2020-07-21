package com.malec.cheesetime.ui.main.cheeseList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.CheeseSort
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CheeseListViewModel @Inject constructor(
    private val repo: CheeseRepo,
    private val router: Router
) : ViewModel(), CheeseAdapter.CheeseAction {
    val cheeseList = MutableLiveData<List<Cheese>>(null)

    val dateFilterStart = MutableLiveData<String>(null)
    val dateFilterEnd = MutableLiveData<String>(null)
    val cheeseTypeFilter = MutableLiveData<String>(null)
    val sortBy = MutableLiveData<String>(null)

    init {
        update()
    }

    fun update() {
        applyFilters()
    }

    fun applyFilters() {
        val sort = when (sortBy.value) {
            "Date (start)" -> CheeseSort.DATE_START
            "Date (end)" -> CheeseSort.DATE_END
            "Cheese type" -> CheeseSort.TYPE
            else -> CheeseSort.ID
        }
        val filter = CheeseFilter(
            dateFilterStart.value,
            dateFilterEnd.value,
            cheeseTypeFilter.value,
            sort
        )
        viewModelScope.launch {
            cheeseList.value = repo.getAllFiltered(filter)
        }
    }

    override fun deleteCheese(cheese: Cheese) {

    }

    override fun editCheese(cheese: Cheese) {
        router.navigateTo(Screens.CheeseManageScreen(cheese))
    }

    override fun selectCheese(cheese: Cheese) {
        TODO("Not yet implemented")
    }
}