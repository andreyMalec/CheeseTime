package com.malec.cheesetime.util

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.malec.cheesetime.BuildConfig
import java.io.File

class CameraIntentCreator(private val context: Context) {
    fun create(): Intent {
        val newMemeFile = getFile()
        val extraFile =
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, newMemeFile)

        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, extraFile)
        }
    }

    fun getFile() = File(context.cacheDir, TEMP_PHOTO_PATH)

    companion object {
        const val TEMP_PHOTO_PATH = "newPhoto.jpg"
    }
}