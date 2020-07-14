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

    init {

    }

    fun setCheese(newCheese: Cheese) {
        if (newCheese.id != 0L) {
            cheese.value = newCheese
            badgeColor.value = newCheese.badgeColor
        }
    }

    fun deleteCheese() {
        cheese.value?.let {
            viewModelScope.launch {
                repo.deleteById(it.id)
            }
        }
    }

    fun checkAndManageCheese() {
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
            return
        }

        val regex =
            Regex("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$")
        if (!mDate.matches(regex)) {
            isInvalidDateError.value = true
            return
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
            mCheese?.id
        )
        if (mCheese == null)
            repo.create(
                cheese,
                {
                    manageResult.value = context.getString(R.string.cheese_created)
                }, { setError(it) }
            )
        else
            repo.update(
                cheese,
                {
                    manageResult.value = context.getString(R.string.cheese_updated)
                }, { setError(it) }
            )
    }

    private fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        manageError.value = msg.drop(i + 2)
    }
}