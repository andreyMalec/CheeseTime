package com.malec.cheesetime.model

import android.graphics.Bitmap
import com.google.firebase.storage.StorageReference

data class Photo(
    val name: String,
    val content: Bitmap?,
    val ref: StorageReference?
)

class PhotoF {
    var name: String = ""
    var content: Bitmap? = null
    var ref: String? = null

    companion object {
        fun from(photo: Photo) = PhotoF().apply {
            name = photo.name
            content = photo.content
            ref = photo.ref?.path
        }
    }
}