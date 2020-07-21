package com.malec.cheesetime.ui.main.cheeseManage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.util.CheeseCreator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CheeseManageViewModel @Inject constructor(
    private val repo: CheeseRepo,
    private val context: Context
) : ViewModel() {

    val isFieldsEmptyError = MutableLiveData(false)
    val isInvalidDateError = MutableLiveData(false)
    val manageError = MutableLiveData<String>(null)
    val manageResult = MutableLiveData<String>(null)

    val name = MutableLiveData<String>("")
    val date = MutableLiveData<String>("")
    val recipe = MutableLiveData<String>("")
    val comment = MutableLiveData<String>("")
    val milkType = MutableLiveData<String>("")
    val milkVolume = MutableLiveData<String>("")
    val milkAge = MutableLiveData<String>("")
    val composition = MutableLiveData<String>("")
    val stages = MutableLiveData<List<String>>(listOf())
    val badgeColor = MutableLiveData<Int>()
    val cheese = MutableLiveData<Cheese>(null)

    fun setCheese(newCheese: Cheese) {
        if (newCheese.id != 0L) {
            cheese.value = newCheese
            badgeColor.value = newCheese.badgeColor
        }
    }

    fun shareCheese() {
        cheese.value?.let {
            repo.share(it)
        }
    }

    fun deleteCheese() {
        cheese.value?.let {
            viewModelScope.launch {
                try {
                    repo.deleteById(it.id)
                    manageResult.value = context.getString(R.string.cheese_deleted)
                } catch (e: Exception) {
                    setError(e)
                }
            }
        }
    }

    fun checkAndManageCheese() = viewModelScope.launch {
        val mName = name.value
        val mDate = date.value
        val mRecipe = recipe.value
        val mComment = comment.value
        val mMilkType = milkType.value
        val mMilkVolume = milkVolume.value
        val mMilkAge = milkAge.value
        val mComposition = composition.value
        val mStages = stages.value
        var mColor = badgeColor.value
        val mCheese = cheese.value

        if (
            mName.isNullOrBlank() || mDate.isNullOrBlank() ||
            mRecipe.isNullOrBlank() || mMilkType.isNullOrBlank() ||
            mMilkVolume.isNullOrBlank() || mMilkAge.isNullOrBlank()
        ) {
            isFieldsEmptyError.value = true
            return@launch
        }

        if (!CheeseCreator.isDateValid(mDate)) {
            isInvalidDateError.value = true
            return@launch
        }

        if (mColor == null || mColor == 0)
            mColor = mCheese?.badgeColor

        val cheese = CheeseCreator.create(
            mName,
            mDate,
            mRecipe,
            mComment,
            mMilkType,
            mMilkVolume,
            mMilkAge,
            mComposition,
            mStages,
            mColor,
            mCheese?.isArchived,
            mCheese?.id ?: repo.getNextId()
        )
        if (mCheese == null)
            try {
                repo.create(cheese)
                manageResult.value = context.getString(R.string.cheese_created)
            } catch (e: Exception) {
                setError(e)
            }
        else
            try {
                repo.update(cheese)
                manageResult.value = context.getString(R.string.cheese_updated)
            } catch (e: Exception) {
                setError(e)
            }
    }

    private fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        manageError.value = msg.drop(i + 2)
    }
}