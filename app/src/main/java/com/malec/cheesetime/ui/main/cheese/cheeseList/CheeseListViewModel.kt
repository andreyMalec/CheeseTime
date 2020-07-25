package com.malec.cheesetime.ui.main.cheese.cheeseList

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
    val selectedCount = MutableLiveData(0)

    val dateFilterStart = MutableLiveData<String>(null)
    val dateFilterEnd = MutableLiveData<String>(null)
    val cheeseTypeFilter = MutableLiveData<String>(null)
    val sortBy = MutableLiveData<String>(null)

    init {
        update()
    }

    fun update() {
        applyFilters()
        selectedCount.value = repo.getSelectedIds().size
    }

    fun applyFilters() {
        val sort = CheeseSort.from(sortBy.value)
        val filter = CheeseFilter(
            dateFilterStart.value,
            dateFilterEnd.value,
            cheeseTypeFilter.value,
            sort
        )
        viewModelScope.launch {
            try {
                cheeseList.value = repo.getAllFiltered(filter)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun archiveSelected() {
        viewModelScope.launch {
            repo.archiveSelected()
            update()
        }
    }

    fun printSelected() {
        viewModelScope.launch {
            repo.share(repo.getAllSelected())
        }
    }

    fun deleteSelected() {
        viewModelScope.launch {
            repo.deleteSelected()
            update()
        }
    }

    fun unselect() {
        viewModelScope.launch {
            repo.unselect()
            update()
        }
    }

    override fun onClick(cheese: Cheese) {
        if (repo.getSelectedIds().isEmpty())
            router.navigateTo(Screens.CheeseManageScreen(cheese))
        else
            onLongClick(cheese)
    }

    override fun onLongClick(cheese: Cheese) {
        repo.toggleSelect(cheese)
        update()
    }
}