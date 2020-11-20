package com.malec.cheesetime.ui.main.cheese.cheeseManage.fullscreenPhoto

import androidx.lifecycle.MutableLiveData
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.model.PhotoF
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.base.BasePhotoViewModel
import com.malec.cheesetime.util.BitmapDecoder
import com.malec.cheesetime.util.PhotoDownloader
import com.malec.cheesetime.util.PhotoSharer
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class FullscreenPhotoViewModel @Inject constructor(
    private val repo: CheeseRepo,
    override val router: Router,
    override val res: Resources,
    override val bitmapDecoder: BitmapDecoder,
    override val photoDownloader: PhotoDownloader,
    override val photoSharer: PhotoSharer
) : BasePhotoViewModel(router, res, bitmapDecoder, photoDownloader, photoSharer) {
    val isDeleted = MutableLiveData<Boolean>(null)

    fun setPhoto(photo: PhotoF?) {
        if (photo == null)
            return

        safeRun {
            fullscreenPhoto.value = repo.convertPhoto(photo)
        }
    }

    override fun setError(t: Throwable?) {}

    override fun onPhotoDeleteClick(photo: Photo) {
        isDeleted.value = true
        super.onPhotoDeleteClick(photo)
        router.exit()
    }
}