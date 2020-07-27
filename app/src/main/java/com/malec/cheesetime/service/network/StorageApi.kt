package com.malec.cheesetime.service.network

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.malec.cheesetime.model.Photo
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class StorageApi(private val storage: FirebaseStorage, private val context: Context) {
    fun getUriById(id: Long) =
        storage.getReferenceFromUrl("gs://cheesetime.appspot.com/$id.jpg")

    fun getUriById(id: String) =
        storage.getReferenceFromUrl("gs://cheesetime.appspot.com/$id.jpg")

    fun save(photo: Photo) {
        if (photo.content != null) {
            val fileName = photo.name + ".jpg"
            val file = createImageFile(fileName)
            savePhoto(photo, file)
            val u = Uri.fromFile(file)
            storage.getReference(fileName).putFile(u)
        }
    }

    private fun createImageFile(path: String): File {
        return File(context.cacheDir, path).apply {
            createNewFile()
        }
    }

    private fun savePhoto(photo: Photo, file: File) {
        if (photo.content != null) {
            val bos = ByteArrayOutputStream()
            photo.content.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapData = bos.toByteArray()

            FileOutputStream(file).apply {
                write(bitmapData)
                flush()
            }
        }
    }
}