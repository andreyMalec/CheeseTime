package com.malec.cheesetime.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.malec.cheesetime.model.Cheese
import java.io.File
import java.io.FileOutputStream
import java.lang.Integer.max

class CheeseSharer(private val context: Context) {
    fun send(cheese: Cheese) {
        shareUri(makeUri(makeBitmap(cheese)))
    }

    private fun shareUri(uri: Uri) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, context.contentResolver.getType(uri))
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        val chooserIntent = Intent.createChooser(
            shareIntent,
            "Choose an app"
        )
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    private fun makeUri(bitmap: Bitmap): Uri {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val newFile = File(cachePath, "image.png")

        val stream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        stream.flush()
        stream.close()

        return FileProvider.getUriForFile(context, context.packageName, newFile)
    }

    private fun makeBitmap(cheese: Cheese): Bitmap {
        val codeImage =
            BarcodeEncoder().encodeBitmap(cheese.id.toString(), BarcodeFormat.CODE_128, 550, 100)
        val textImage = textToBitmap(cheese.name + " id: " + cheese.id, codeImage.height / 2F)

        val w = max(codeImage.width, textImage.width)
        val h = codeImage.height + textImage.height + 10

        val image = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val codeImageStart = w / 2F - codeImage.width / 2F
        val textImageStart = w / 2F - textImage.width / 2F

        val canvas = Canvas(image)
        canvas.drawBitmap(codeImage, codeImageStart, 0F, null)
        canvas.drawBitmap(textImage, textImageStart, codeImage.height + 10F, null)

        return image
    }

    private fun textToBitmap(text: String, textSize: Float): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent()

        val image = Bitmap.createBitmap(
            (paint.measureText(text).toInt()),
            ((baseline + paint.descent()).toInt()), Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(image)
        canvas.drawText(text, 0F, baseline, paint)
        return image
    }
}