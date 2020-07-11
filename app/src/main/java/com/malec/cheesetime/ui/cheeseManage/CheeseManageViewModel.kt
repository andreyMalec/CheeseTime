package com.malec.cheesetime.ui.cheeseManage

import android.util.Log
import androidx.lifecycle.*
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.repo.CheeseRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@ExperimentalCoroutinesApi
class CheeseManageViewModel @Inject constructor(
    private val repo: CheeseRepo
) : ViewModel() {

    private val _isManageError = MutableLiveData(false)
    val isManageError: LiveData<Boolean>
        get() = _isManageError

    val badgeColor = MutableLiveData<Int>()
    val cheeseId = MutableLiveData<Long>(null)
    val cheese: LiveData<Cheese>

    init {
        cheese = cheeseId.asFlow().flatMapLatest { id ->
            repo.getById(id)
        }.asLiveData()
    }

    fun deleteCheese() {
        cheeseId.value?.let {
            viewModelScope.launch {
                repo.deleteById(it)
            }
        }
    }

    fun getAll() {
        viewModelScope.launch {
            Log.e("test", "testMessage: " + repo.getAll().first())
        }
    }

    fun checkAndManageCheese(
        name: String?,
        date: String?,
        recipe: String?,
        comment: String?,
        milk: String?,
        composition: String?,
        stages: String?
    ) {
        if (name.isNullOrBlank() || date.isNullOrBlank() || recipe.isNullOrBlank() || milk.isNullOrBlank()) {
            _isManageError.value = true
            return
        } else {
            val regex =
                Regex("^(?:(?:(?:0?[13578]|1[02])(\\/|-|\\.)31)\\1|(?:(?:0?[1,3-9]|1[0-2])(\\/|-|\\.)(?:29|30)\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:0?2(\\/|-|\\.)29\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:(?:0?[1-9])|(?:1[0-2]))(\\/|-|\\.)(?:0?[1-9]|1\\d|2[0-8])\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$")
            if (!date.matches(regex)) {
                _isManageError.value = true
                return
            }
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val dateFormatted = format.parse(date)?.time ?: 0

            viewModelScope.launch {
                var color = badgeColor.value
                if (color == null || color == 0)
                    color = cheese.value?.badgeColor

                val cheese = cheese.value
                if (cheese == null)
                    repo.create(
                        name,
                        dateFormatted,
                        recipe,
                        comment,
                        milk,
                        composition,
                        stages,
                        color
                    )
                else
                    repo.update(
                        name,
                        dateFormatted,
                        recipe,
                        comment,
                        milk,
                        composition,
                        stages,
                        color,
                        cheese
                    )
            }
        }
    }
}