package com.malec.cheesetime.ui.main.cheese.cheeseList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import com.malec.cheeselist.presentation.view.CheeseAdapter
import com.malec.cheesetime.ui.base.BaseViewModel
import com.malec.domain.model.Cheese
import com.malec.domain.model.CheeseSort
import com.malec.domain.repository.CheeseRepo
import com.malec.presentation.Resources
import javax.inject.Inject

class CheeseListViewModel @Inject constructor(
    private val repo: CheeseRepo,
    private val userRepo: com.malec.domain.repository.UserRepo,
    private val router: Router,
    private val res: Resources
) : BaseViewModel(), CheeseAdapter.CheeseAction {
    //    val cheeseList: LiveData<List<Cheese>>
    val selectedCount = MutableLiveData(0)

    val searchQuery = MutableLiveData<String?>(null)

    val dateFilterStart = MutableLiveData<String?>(null)
    val dateFilterEnd = MutableLiveData<String?>(null)
    val cheeseTypeFilter = MutableLiveData<String?>(null)
    val archivedFilter = MutableLiveData(false)
    val sortBy = MutableLiveData<CheeseSort?>(null)

    val cheeseTypes = MutableLiveData<List<String>>()

    init {
//        update()
//
//        cheeseList = searchQuery.asFlow().flatMapLatest { query ->
//            val filter = CheeseFilter(
//                dateFilterStart.value,
//                dateFilterEnd.value,
//                cheeseTypeFilter.value,
//                archivedFilter.value,
//                sortBy.value
//            )
//            if (query.isNullOrBlank())
//                repo.getAllFilteredFlow(filter)
//            else
//                repo.getAllTitleContainsFlow(filter, query)
//        }.asLiveData()
//
//        safeRun {
//            cheeseTypes.value = (
//                    userRepo.getRecipes().map { it.name }.takeIf { it.isNotEmpty() }
//                        ?: res.recipes()
//                    ).let { listOf(res.stringCheeseTypeAny()) + it }
//        }
    }

    fun update() {
        searchQuery.value = null
//        selectedCount.value = repo.getSelectedIds().size
    }

    fun archiveSelected() {
//        safeRun {
//            repo.archiveSelected()
//            update()
//        }
    }

    fun printSelected() {
        safeRun {
            repo.share(repo.getAllSelected())
        }
    }

    fun deleteSelected() {
//        safeRun {
//            repo.deleteSelected()
//            update()
//        }
    }

    fun unselect() {
//        safeRun {
//            repo.unselect()
//            update()
//        }
    }

    fun onSwipe(cheese: Cheese) {
//        safeRun {
//            repo.delete(cheese.id)
//            update()
//        }
    }

    override fun onClick(cheese: Cheese) {
//        if (repo.getSelectedIds().isEmpty())
//            router.navigateTo(Screens.cheeseManage(cheese))
//        else
//            onLongClick(cheese)
    }

    override fun onLongClick(cheese: Cheese) {
//        repo.toggleSelect(cheese)
        update()
    }

    override fun setError(t: Throwable?) {
        Log.e("CheeseListViewModel", "setError: $t")
    }
}