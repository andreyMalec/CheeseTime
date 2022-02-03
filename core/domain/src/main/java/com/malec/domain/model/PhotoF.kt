package com.malec.domain.model

import android.graphics.Bitmap

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