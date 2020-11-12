package com.malec.cheesetime.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

class BitmapDecoder(private val context: Context) {
    fun fromUri(uri: Uri?): Bitmap? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (uri == null)
                null
            else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        } else
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    fun fromCamera(): Bitmap? =
        BitmapFactory.decodeFile(CameraIntentCreator(context).getFile().path)
}