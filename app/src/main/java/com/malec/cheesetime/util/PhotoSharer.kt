package com.malec.cheesetime.util

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.malec.domain.model.Photo
import com.malec.domain.util.UriSharer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

class PhotoSharer(private val context: Context) : UriSharer(context) {
    suspend fun send(photo: Photo) {
        withContext(Dispatchers.IO) {
            shareUri(makeUri("${photo.name}.jpeg", getImage(photo)))
        }
    }

    private fun getImage(photo: Photo): Bitmap {
        return photo.content.takeIf { it != null } ?: Glide.with(context).asBitmap().load(photo.ref)
            .submit().get()
    }

    override fun writeToStream(content: Any, stream: FileOutputStream) {
        if (content is Bitmap)
            content.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        else throw IllegalArgumentException("Unsupported type")
    }
}