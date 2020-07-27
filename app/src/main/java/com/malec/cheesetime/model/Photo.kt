package com.malec.cheesetime.model

import android.graphics.Bitmap
import com.google.firebase.storage.StorageReference

data class Photo(
    val name: String,
    val content: Bitmap?,
    val ref: StorageReference?
)