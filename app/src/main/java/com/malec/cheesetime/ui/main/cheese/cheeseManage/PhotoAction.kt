package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.view.View
import androidx.core.util.Pair
import com.malec.cheesetime.model.Photo

interface PhotoAction {
    fun onPhotoClick(photo: Photo, vararg transitionOptions: Pair<View, String>)

    fun onPhotoLongClick(photo: Photo)
}