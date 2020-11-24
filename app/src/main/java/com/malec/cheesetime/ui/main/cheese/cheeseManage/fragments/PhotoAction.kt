package com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments

import com.malec.cheesetime.model.Photo

interface PhotoAction {
    fun onPhotoClick(photo: Photo)

    fun onPhotoLongClick(photo: Photo)
}