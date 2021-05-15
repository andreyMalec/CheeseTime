package com.malec.cheesetime.ui.main.cheese.cheeseList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.github.terrakok.cicerone.Router
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.CheeseSort
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.base.BaseViewModel
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class CheeseListViewModel @Inject constructor(
    private val repo: CheeseRepo,
    private val userRepo: UserRepo,
    private val router: Router,
    private val res: Resources
) : BaseViewModel(), CheeseAdapter.CheeseAction {
    val cheeseList: LiveData<List<Cheese>>
    val selectedCount = MutableLiveData(0)

    val searchQuery = MutableLiveData<String?>(null)

    val dateFilterStart = MutableLiveData<String?>(null)
    val dateFilterEnd = MutableLiveData<String?>(null)
    val cheeseTypeFilter = MutableLiveData<String?>(null)
    val archivedFilter = MutableLiveData(false)
    val sortBy = MutableLiveData<CheeseSort?>(null)

    val cheeseTypes = MutableLiveData<List<String>>()

    init {
        update()

        cheeseList = searchQuery.asFlow().flatMapLatest { query ->
            val filter = CheeseFilter(
                dateFilterStart.value,
                dateFilterEnd.value,
                cheeseTypeFilter.value,
                archivedFilter.value,
                sortBy.value
            )
            if (query.isNullOrBlank())
                repo.getAllFiltered(filter)
            else
                repo.getAllTitleContains(filter, query)
        }.asLiveData()

        safeRun {
            cheeseTypes.value = (
                    userRepo.getRecipes().map { it.name }.takeIf { it.isNotEmpty() }
                        ?: res.recipes()
                    ).let { listOf(res.stringCheeseTypeAny()) + it }
        }
    }

    fun update() {
        searchQuery.value = null
        selectedCount.value = repo.getSelectedIds().size
    }

    fun archiveSelected() {
        safeRun {
            repo.archiveSelected()
            update()
        }
    }

    fun printSelected() {
        safeRun {
            repo.share(repo.getAllSelected())
        }
    }

    fun deleteSelected() {
        safeRun {
            repo.deleteSelected()
            update()
        }
    }

    fun unselect() {
        safeRun {
            repo.unselect()
            update()
        }
    }

    fun onSwipe(cheese: Cheese) {
        safeRun {
            repo.deleteById(cheese.id)
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
        Log.e("CheeseListViewModel", "setError: $t")
    }
}