package com.malec.cheesetime.ui.base

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.util.BitmapDecoder
import com.malec.cheesetime.util.PhotoDownloader
import com.malec.cheesetime.util.PhotoSharer
import ru.terrakok.cicerone.Router
import java.util.*

abstract class BasePhotoViewModel(
    protected open val router: Router,
    protected open val res: Resources,
    protected open val bitmapDecoder: BitmapDecoder,
    protected open val photoDownloader: PhotoDownloader,
    protected open val photoSharer: PhotoSharer
) : BaseViewModel() {
    private val downloadedPhoto = MutableLiveData<Photo>(null)

    val photos = MutableLiveData<List<Photo>>(listOf())
    val photoManageResult = MutableLiveData<String>(null)

    val fullscreenPhoto = MutableLiveData<Photo>(null)

    fun setDownloadedPhoto(photo: Photo) {
        downloadedPhoto.value = photo
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

    fun onPhotoShareClick(photo: Photo?) {
        photo?.let {
            safeRun {
                photoSharer.send(it)
            }
        }
    }

    open fun onPhotoDeleteClick(photo: Photo) {
        fullscreenPhoto.value = null
        photos.value = photos.value?.toMutableList()?.apply {
            remove(photo)
        }
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
}