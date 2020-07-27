package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.util.CameraIntentCreator
import com.malec.cheesetime.util.CheeseCreator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CheeseManageViewModel @Inject constructor(
    private val repo: CheeseRepo,
    private val context: Context,
    private val router: Router
) : ViewModel(), PhotoAdapter.PhotoAction {

    val isFieldsEmptyError = MutableLiveData(false)
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
    val photos = MutableLiveData<List<Photo>>(listOf())
    val cheese = MutableLiveData<Cheese>(null)

    val pickedImage = MutableLiveData<Bitmap>(null)

    companion object {
        const val GALLERY = Screens.GalleryPickScreen.requestCode
        const val CAMERA = Screens.CameraPickScreen.requestCode
    }

    fun setCheese(newCheese: Cheese) {
        if (newCheese.id != 0L) {
            cheese.value = newCheese
            photos.value = repo.getPhotoUriById(newCheese.photo)
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

    fun onAttachFromGallery() {
        router.navigateTo(Screens.GalleryPickScreen)
    }

    fun onAttachFromCamera() {
        router.navigateTo(Screens.CameraPickScreen)
    }

    fun getImageFromResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val bitmap: Bitmap? = when {
            resultCode == Activity.RESULT_OK && data != null && data.data != null && requestCode == GALLERY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, data.data!!)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, data.data)
                }
            }
            resultCode == Activity.RESULT_OK && requestCode == CAMERA -> {
                val newFile = CameraIntentCreator(context).getFile()

                BitmapFactory.decodeFile(newFile.path)
            }
            else -> null
        }

        bitmap?.let {
            pickedImage.value = bitmap
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
        val mPhotos = photos.value
        val mCheese = cheese.value

        if (
            mName.isNullOrBlank() || mDate.isNullOrBlank() ||
            mRecipe.isNullOrBlank() || mMilkType.isNullOrBlank() ||
            mMilkVolume.isNullOrBlank() || mMilkAge.isNullOrBlank()
        ) {
            isFieldsEmptyError.value = true
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
            mPhotos,
            mCheese?.id ?: getNextId(),
            mCheese?.dateStart
        )
        try {
            repo.savePhotos(mPhotos)
            manageResult.value = if (mCheese == null) {
                repo.create(cheese)
                context.getString(R.string.cheese_created)
            } else {
                repo.update(cheese)
                context.getString(R.string.cheese_updated)
            }
        } catch (e: Exception) {
            setError(e)
        }
    }

    private suspend fun getNextId() = try {
        repo.getNextId()
    } catch (e: Exception) {
        e.printStackTrace()
        1L
    }

    private fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        manageError.value = msg.drop(i + 2)
    }

    override fun onClick(photo: Photo) {

    }

    override fun onLongClick(photo: Photo) {

    }
}