package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.model.Recipe
import com.malec.cheesetime.model.StringValue
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.main.ManageViewModel
import com.malec.cheesetime.util.BitmapDecoder
import com.malec.cheesetime.util.CheeseCreator
import com.malec.cheesetime.util.PhotoDownloader
import com.malec.cheesetime.util.PhotoSharer
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
) : ManageViewModel(), StringAdapter.RemovableEditTextAction {
    val photoManageResult = MutableLiveData<String>(null)

    private val downloadedPhoto = MutableLiveData<Photo>(null)

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
        safeRun {
            mRecipes.addAll(userRepo.getRecipes())
            recipes.value =
                (mRecipes.map { it.name }.takeIf { it.isNotEmpty() } ?: res.recipes()).let {
                    cheese.value?.recipe?.let { currentRecipe ->
                        if (!it.contains(currentRecipe) && currentRecipe.isNotBlank())
                            listOf(currentRecipe) + it
                        else
                            it
                    } ?: it
                }
        }
    }

    override fun checkCanSave() {
        val c = cheese.value
        _isSaveActive.value =
            !(c?.name.isNullOrBlank() || c?.milkType.isNullOrBlank() || c?.milkVolume.isNullOrBlank())
    }

    fun setCheese(newCheese: Cheese?) {
        if (cheese.value == null) {
            cheese.value = if (newCheese != null) {
                _isDeleteActive.value = true
                stages.value =
                    newCheese.stages.getStages()
                photos.value = repo.getPhotosById(newCheese.photo)
                badgeColor.value = newCheese.badgeColor
                newCheese
            } else
                CheeseCreator.empty()
        }
        checkCanSave()
    }

    fun shareCheese() {
        cheese.value?.let {
            repo.share(it)
        }
    }

    private fun String?.getStages() = this?.split("♂")?.map { StringValue(it) }?.toMutableList()

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
            safeRun {
                repo.deleteById(it.id)
                repo.deletePhotos(photos.value)
                manageResult.value = res.stringCheeseDeleted()
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

    fun setDownloadedPhoto(photo: Photo) {
        downloadedPhoto.value = photo
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

    fun checkAndManageCheese() {
        val mCheese = cheese.value
        val mStages = stages.value
        var mColor = badgeColor.value
        val mPhotos = photos.value

        if (mColor == null || mColor == 0)
            mColor = mCheese?.badgeColor

        safeRun {
            val cheese = CheeseCreator.create(
                mCheese?.name,
                mCheese?.date,
                mCheese?.recipe,
                mCheese?.comment,
                mCheese?.milkType,
                mCheese?.milkVolume,
                mCheese?.milkAge,
                mCheese?.composition,
                mStages?.map { it.data },
                mColor,
                mCheese?.isArchived,
                mPhotos,
                mCheese?.id.takeIf { it != 0L } ?: repo.getNextId(),
                mCheese?.dateStart
            )

            repo.updatePhotos(mCheese?.photo, mPhotos)
            manageResult.value = if (mCheese?.id == 0L) {
                repo.create(cheese)
                res.stringCheeseCreated()
            } else {
                repo.update(cheese)
                res.stringCheeseUpdated()
            }
        }
    }

    fun onPhotoDeleteClick(photo: Photo) {
        photos.value = photos.value?.toMutableList()?.apply {
            remove(photo)
        }
    }

    fun onPhotoDownloadClick() {
        safeRun {
            downloadedPhoto.value?.let {
                photoDownloader.download(it)
                downloadedPhoto.value = null
                photoManageResult.value = res.stringPhotoDownloadedSuccessful()
            }
        }
    }

    fun onPhotoShareClick(photo: Photo) {
        safeRun {
            photoSharer.send(photo)
        }
    }
}