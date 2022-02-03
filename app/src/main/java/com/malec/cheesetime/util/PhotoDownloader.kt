package com.malec.cheesetime.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.malec.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class PhotoDownloader(private val context: Context) {
    companion object {
        private const val folderName = "Pictures/CheeseTime"
    }

    suspend fun download(photo: Photo) {
        withContext((Dispatchers.IO)) {
            saveImage(getImage(photo), photo.name + ".jpeg")
        }
    }

    private fun getImage(photo: Photo): Bitmap {
        return photo.content.takeIf { it != null } ?: Glide.with(context).asBitmap().load(photo.ref)
            .submit().get()
    }

    private fun saveImage(bitmap: Bitmap, fileName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = contentValues().apply {
                put(MediaStore.Images.Media.RELATIVE_PATH, folderName)
                put(MediaStore.Images.Media.IS_PENDING, true)
            }

            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?.let {
                    saveImageToStream(bitmap, context.contentResolver.openOutputStream(it))
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(it, values, null, null)
                }
        } else {
            val directory =
                File(Environment.getExternalStorageDirectory().toString() + "/$folderName").also {
                    if (!it.exists())
                        it.mkdirs()
                }

            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            val values = contentValues().apply {
                put(MediaStore.Images.Media.DATA, file.absolutePath)
            }
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
    }

    private fun contentValues(): ContentValues {
        return ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        outputStream?.let {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.close()
        }
    }
}