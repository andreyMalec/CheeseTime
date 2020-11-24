package com.malec.cheesetime.ui.main.cheese.cheeseList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.CheeseSort
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheeseListViewModel @Inject constructor(
    private val repo: CheeseRepo,
    private val userRepo: UserRepo,
    private val router: Router,
    private val res: Resources
) : BaseViewModel(), CheeseAdapter.CheeseAction {
    val cheeseList = MutableLiveData<List<Cheese>>(null)
    val selectedCount = MutableLiveData(0)

    val dateFilterStart = MutableLiveData<String>(null)
    val dateFilterEnd = MutableLiveData<String>(null)
    val cheeseTypeFilter = MutableLiveData<String>(null)
    val archivedFilter = MutableLiveData(false)
    val sortBy = MutableLiveData<CheeseSort>(null)

    val cheeseTypes = MutableLiveData<List<String>>()

    init {
        update()

        viewModelScope.launch {
            cheeseTypes.value = (
                    userRepo.getRecipes().map { it.name }.takeIf { it.isNotEmpty() }
                        ?: res.recipes()
                    ).let { listOf(res.stringCheeseTypeAny()) + it }
        }
    }

    fun update() {
        applyFilters()
        selectedCount.value = repo.getSelectedIds().size
    }

    fun applyFilters() {
        safeRun {
            cheeseList.value = repo.getAllFiltered(
                CheeseFilter(
                    dateFilterStart.value,
                    dateFilterEnd.value,
                    cheeseTypeFilter.value,
                    archivedFilter.value,
                    sortBy.value
                )
            )
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
            router.navigateTo(Screens.cheeseManage(cheese))
        else
            onLongClick(cheese)
    }

    override fun onLongClick(cheese: Cheese) {
        repo.toggleSelect(cheese)
        update()
    }

    override fun setError(t: Throwable?) {
        Log.e("test", "testMessage: " + t)
    }
}