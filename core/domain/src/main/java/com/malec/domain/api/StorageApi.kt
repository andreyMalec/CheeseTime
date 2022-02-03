package com.malec.domain.api

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.malec.domain.model.Photo
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class StorageApi(private val storage: FirebaseStorage, private val cacheDir: File) {
    fun getRefById(id: Long) =
        storage.getReferenceFromUrl("gs://cheesetime.appspot.com/$id.jpg")

    fun getRefById(id: String) =
        storage.getReferenceFromUrl("gs://cheesetime.appspot.com/$id.jpg")

    suspend fun save(photo: Photo) {
        if (photo.content != null) {
            val fileName = photo.name + ".jpg"
            val file = createImageFile(fileName)
            savePhoto(photo, file)
            val u = Uri.fromFile(file)
            storage.getReference(fileName).putFile(u).await()
        }
    }

    suspend fun delete(photo: Photo) {
        getRefById(photo.name).delete().await()
    }

    suspend fun deleteById(id: String) {
        getRefById(id).delete().await()
    }

    private fun createImageFile(path: String): File {
        return File(cacheDir, path).apply {
            createNewFile()
        }
    }

    private fun savePhoto(photo: Photo, file: File) {
        if (photo.content != null) {
            val bos = ByteArrayOutputStream()
            photo.content.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapData = bos.toByteArray()

            FileOutputStream(file).use {
                it.write(bitmapData)
                it.flush()
            }
        }
    }
}