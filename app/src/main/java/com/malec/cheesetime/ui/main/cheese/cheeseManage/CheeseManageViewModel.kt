package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.model.Recipe
import com.malec.cheesetime.model.StringValue
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.util.BitmapDecoder
import com.malec.cheesetime.util.CheeseCreator
import com.malec.cheesetime.util.PhotoDownloader
import com.malec.cheesetime.util.PhotoSharer
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

class CheeseManageViewModel @Inject constructor(
    private val repo: CheeseRepo,
    private val router: Router,
    private val userRepo: UserRepo,
    private val res: Resources,
    private val bitmapDecoder: BitmapDecoder,
    private val photoDownloader: PhotoDownloader,
    private val photoSharer: PhotoSharer
) : ViewModel(), StringAdapter.RemovableEdittextAction {

    val isFieldsEmptyError = MutableLiveData(false)
    val manageError = MutableLiveData<String>(null)
    val manageResult = MutableLiveData<String>(null)
    val photoManageResult = MutableLiveData<String>(null)

    private val _tmpPhoto = MutableLiveData<Photo>(null)
    val tmpPhoto: LiveData<Photo>
        get() = _tmpPhoto

    private val _isDoneActive = MutableLiveData(false)
    val isDoneActive: LiveData<Boolean>
        get() = _isDoneActive

    val cheese = MutableLiveData<Cheese>(null)
    val stages = MutableLiveData<MutableList<StringValue>>(mutableListOf())
    val badgeColor = MutableLiveData<Int>()
    val photos = MutableLiveData<List<Photo>>(listOf())

    val recipes = MutableLiveData<List<String>>()

    private val mRecipes = mutableListOf<Recipe>()

    private var isStagesFirstLoad = true

    companion object {
        const val GALLERY = Screens.GalleryPickScreen.requestCode
        const val CAMERA = Screens.CameraPickScreen.requestCode
        const val STORAGE = 5
    }

    init {
        viewModelScope.launch {
            mRecipes.addAll(userRepo.getRecipes())
            recipes.value =
                (userRepo.getRecipes().map { it.name }
                    .takeIf { it.isNotEmpty() } ?: res.recipes()).let {
                    cheese.value?.recipe?.let { currentRecipe ->
                        if (!it.contains(currentRecipe))
                            listOf(currentRecipe) + it
                        else
                            it
                    } ?: it
                }
        }
    }

    fun checkCanSave() {
        val c = cheese.value
        _isDoneActive.value =
            !(c?.name.isNullOrBlank() || c?.milkType.isNullOrBlank() || c?.milkVolume.isNullOrBlank())
    }

    fun setCheese(newCheese: Cheese?) {
        if (newCheese != null) {
            cheese.value = newCheese
            stages.value = newCheese.stages.getStages()?.map { StringValue(it) }?.toMutableList()
            photos.value = repo.getPhotosById(newCheese.photo)
            badgeColor.value = newCheese.badgeColor
        } else
            cheese.value = CheeseCreator.empty()
        checkCanSave()
    }

    fun shareCheese() {
        cheese.value?.let {
            repo.share(it)
        }
    }

    private fun String?.getStages() = this?.split("♂")

    fun selectRecipe(recipeName: String?) {

        if (isStagesFirstLoad) {
            isStagesFirstLoad = false
            return
        }

        if (stages.value.isNullOrEmpty() ||
            stages.value?.first()?.data.isNullOrBlank()
        )
            mRecipes.find { it.name == recipeName }?.stages?.split("♂")?.let { st ->
                stages.value = st.map { StringValue(it) }.toMutableList()
            }
    }

    fun deleteCheese() {
        cheese.value?.let {
            viewModelScope.launch {
                try {
                    repo.deleteById(it.id)
                    repo.deletePhotos(photos.value)
                    manageResult.value = res.stringCheeseDeleted()
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

    override fun onRemoveClick(value: StringValue?) {
        val rm = stages.value?.find { it.data == value?.data }
        stages.value?.remove(rm)
        stages.value = stages.value
    }

    fun addNewStage() {
        stages.value?.add(StringValue(""))
        stages.value = stages.value
    }

    fun setTmpPhoto(photo: Photo) {
        _tmpPhoto.value = photo
    }

    fun getImageFromUri(uri: Uri?) {
        addPhoto(bitmapDecoder.fromUri(uri))
    }

    fun getImageFromCamera() {
        addPhoto(bitmapDecoder.fromCamera())
    }

    private fun addPhoto(bitmap: Bitmap?) {
        bitmap?.let {
            photos.value = photos.value?.let {
                val photo = Photo(Date().time.toString(), bitmap, null)
                it + photo
            }
        }
    }

    fun checkAndManageCheese() = viewModelScope.launch {
        val mCheese = cheese.value
        val mName = mCheese?.name
        val mDate = mCheese?.date
        val mRecipe = mCheese?.recipe
        val mComment = mCheese?.comment
        val mMilkType = mCheese?.milkType
        val mMilkVolume = mCheese?.milkVolume
        val mMilkAge = mCheese?.milkAge
        val mComposition = mCheese?.composition
        val mStages = stages.value
        var mColor = badgeColor.value
        val mPhotos = photos.value

        if (mMilkType.isNullOrBlank() || mMilkVolume.isNullOrBlank()) {
            isFieldsEmptyError.value = true
            return@launch
        }

        if (mColor == null || mColor == 0)
            mColor = mCheese.badgeColor

        val cheese = CheeseCreator.create(
            mName,
            mDate,
            mRecipe,
            mComment,
            mMilkType,
            mMilkVolume,
            mMilkAge,
            mComposition,
            mStages?.map { it.data },
            mColor,
            mCheese.isArchived,
            mPhotos,
            mCheese.id.takeIf { it != 0L } ?: getNextId(),
            mCheese.dateStart
        )
        try {
            repo.updatePhotos(mCheese.photo, mPhotos)
            manageResult.value = if (mCheese.id == 0L) {
                repo.create(cheese)
                res.stringCheeseCreated()
            } else {
                repo.update(cheese)
                res.stringCheeseUpdated()
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

    fun onPhotoDeleteClick(photo: Photo) {
        photos.value = photos.value?.toMutableList()?.apply {
            remove(photo)
        }
    }

    fun onPhotoDownloadClick(photo: Photo) {
        viewModelScope.launch {
            try {
                photoDownloader.download(photo)
                photoManageResult.value = res.stringPhotoDownloadedSuccessful()
                _tmpPhoto.value = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onPhotoShareClick(photo: Photo) {
        viewModelScope.launch {
            try {
                photoSharer.send(photo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}