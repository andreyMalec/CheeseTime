package com.malec.cheesetime.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.malec.cheesetime.model.Cheese
import java.io.File
import java.io.FileOutputStream
import java.lang.Integer.max

class CheeseSharer(private val context: Context) {
    private val PAGE_WIDTH = 420
    private val PAGE_HEIGHT = 594
    private val MARGIN_TOP = 10F
    private val CODE_WIDTH = 105
    private val CODE_HEIGHT = 19
    private val TEXT_SIZE = 8F

    private val MAX_ROWS = 15

    fun send(cheese: Cheese) {
        send(listOf(cheese))
    }

    fun send(cheeseList: List<Cheese>) {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = doc.startPage(pageInfo)
        drawBarcodeList(page.canvas, cheeseList)
        doc.finishPage(page)
        shareUri(makeUri(doc))
        doc.close()
    }

    private fun drawBarcodeList(canvas: Canvas, cheeseList: List<Cheese>) {
        var verticalOffset = MARGIN_TOP / 2
        var horizontalOffset = 0F

        fun draw(cheese: Cheese) {
            val codeImage = BarcodeEncoder().encodeBitmap(
                cheese.id.toString(),
                BarcodeFormat.CODE_128,
                CODE_WIDTH,
                CODE_HEIGHT
            )
            val label = cheese.name + " id: " + cheese.id
            verticalOffset += canvas.drawBarcode(
                codeImage,
                label,
                verticalOffset,
                horizontalOffset
            ) + MARGIN_TOP
        }

        if (cheeseList.size > MAX_ROWS) {
            val maxTextLengthCheese = cheeseList.maxBy { (it.name + " id: " + it.id).length }
            val textW =
                paintForText().measureText(maxTextLengthCheese?.name + " id: " + maxTextLengthCheese?.id)
            val maxW = max(textW.toInt(), CODE_WIDTH)

            val columnCount = PAGE_WIDTH / maxW
            for (i in (0..columnCount)) {
                for (j in (i * MAX_ROWS until i * MAX_ROWS + MAX_ROWS)) {
                    if (j > cheeseList.size - 1)
                        break

                    draw(cheeseList[j])
                }
                verticalOffset = MARGIN_TOP / 2
                horizontalOffset += maxW
            }
        } else {
            for (cheese in cheeseList)
                draw(cheese)
        }
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

    private fun makeUri(document: PdfDocument): Uri {
        val cachePath = File(context.cacheDir, "docs")
        cachePath.mkdirs()
        val newFile = File(cachePath, "doc.pdf")

        val stream = FileOutputStream(newFile)
        document.writeTo(stream)

        stream.flush()
        stream.close()

        return FileProvider.getUriForFile(context, context.packageName, newFile)
    }

    private fun Canvas.drawBarcode(
        codeImage: Bitmap,
        label: String,
        verticalOffset: Float,
        horizontalOffset: Float
    ): Float {
        val paint = paintForText()
        val baseline = -paint.ascent()

        val textW = paint.measureText(label).toInt()
        val textH = (baseline + paint.descent()).toInt()

        val maxWidth = max(codeImage.width, textW)

        val codeImageStart = maxWidth / 2F - codeImage.width / 2F
        val textImageStart = maxWidth / 2F - textW / 2F

        drawBitmap(codeImage, horizontalOffset + codeImageStart, verticalOffset, null)
        drawText(
            label,
            horizontalOffset + textImageStart,
            verticalOffset + baseline + codeImage.height,
            paint
        )

        return (textH + codeImage.height).toFloat()
    }

    private fun paintForText() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }
}