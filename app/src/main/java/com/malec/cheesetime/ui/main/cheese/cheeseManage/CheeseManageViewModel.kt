package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.MutableLiveData
import com.malec.cheesetime.model.*
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.base.BasePhotoViewModel
import com.malec.cheesetime.ui.base.ManageViewModel
import com.malec.cheesetime.util.BitmapDecoder
import com.malec.cheesetime.util.PhotoDownloader
import com.malec.cheesetime.util.PhotoSharer
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class CheeseManageViewModel @Inject constructor(
    private val repo: CheeseRepo,
    override val router: Router,
    private val userRepo: UserRepo,
    override val res: Resources,
    override val bitmapDecoder: BitmapDecoder,
    override val photoDownloader: PhotoDownloader,
    override val photoSharer: PhotoSharer
) : BasePhotoViewModel(router, res, bitmapDecoder, photoDownloader, photoSharer), ManageViewModel,
    StringAdapter.RemovableEditTextAction {

    override val manageError = MutableLiveData<String>(null)
    override val manageResult = MutableLiveData<String>(null)
    override val isSaveActive = MutableLiveData(false)
    override val isDeleteActive = MutableLiveData(false)

    val cheese = MutableLiveData<Cheese>(null)
    val stages = MutableLiveData<MutableList<StringValue>>(mutableListOf())
    val badgeColor = MutableLiveData<Int>()

    val recipes = MutableLiveData<List<String>>()

    private val mRecipes = mutableListOf<Recipe>()

    private var isStagesFirstLoad = true

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
        isSaveActive.value =
            !(c?.name.isNullOrBlank() || c?.milkType.isNullOrBlank() || c?.milkVolume.isNullOrBlank())
    }

    fun setCheese(newCheese: Cheese?) {
        if (cheese.value == null) {
            cheese.value = if (newCheese != null) {
                isDeleteActive.value = true
                stages.value =
                    newCheese.stages.map { StringValue(it) }.toMutableList()
                photos.value = repo.getPhotosById(newCheese.photos)
                badgeColor.value = newCheese.badgeColor
                newCheese
            } else
                Cheese.empty()
        }
        checkCanSave()
    }

    fun shareCheese() {
        cheese.value?.let {
            repo.share(it)
        }
    }

    fun selectRecipe(recipeName: String?) {
        if (isStagesFirstLoad) {
            isStagesFirstLoad = false
            return
        }

        if (stages.value.isNullOrEmpty() ||
            stages.value?.first()?.data.isNullOrBlank()
        )
            mRecipes.find { it.name == recipeName }?.stages?.let { stage ->
                stages.value = stage.map { StringValue(it) }.toMutableList()
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

    fun onPhotoClick(photo: Photo, transitionOptions: Pair<View, String>) {
        fullscreenPhoto.value = photo
        router.navigateTo(Screens.FullscreenPhotoScreen(PhotoF.from(photo), transitionOptions))
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

    fun checkAndManageCheese() {
        val mCheese = cheese.value
        val mStages = stages.value
        val mColor = badgeColor.value
        val mPhotos = photos.value

        safeRun {
            val cheese = Cheese(
                mCheese?.id.takeIf { it != 0L } ?: repo.getNextId(),
                mCheese?.name,
                mCheese?.date,
                mCheese?.dateStart,
                mCheese?.recipe,
                mCheese?.comment,
                mCheese?.milkType,
                mCheese?.milkVolume,
                mCheese?.milkAge,
                mCheese?.composition,
                mStages?.map { it.data },
                mColor,
                mCheese?.isArchived,
                mPhotos?.map { it.name }
            )

            repo.updatePhotos(mCheese?.photos, mPhotos)
            manageResult.value = if (cheese.id == 0L) {
                repo.create(cheese)
                res.stringCheeseCreated()
            } else {
                repo.update(cheese)
                res.stringCheeseUpdated()
            }
        }
    }

    override fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        manageError.value = msg.drop(i + 2)
    }
}