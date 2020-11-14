package com.malec.cheesetime.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.malec.cheesetime.model.Cheese
import java.io.FileOutputStream
import java.lang.Integer.max

class CheeseSharer(context: Context) : UriSharer(context) {
    companion object {
        private const val PAGE_WIDTH = 630
        private const val PAGE_HEIGHT = 891
        private const val MARGIN_TOP = 15F
        private const val CODE_WIDTH = 156
        private const val CODE_HEIGHT = 27
        private const val TEXT_SIZE = 12F

        private const val MAX_ROWS = 17
    }

    private val paintForText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }

    fun send(cheese: Cheese) {
        send(listOf(cheese))
    }

    fun send(cheeseList: List<Cheese>) {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = doc.startPage(pageInfo)
        drawBarcodeList(page.canvas, cheeseList)
        doc.finishPage(page)
        shareUri(makeUri("doc.pdf", doc))
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
            val maxTextLengthCheese = cheeseList.maxByOrNull { (it.name + " id: " + it.id).length }
            val textW =
                paintForText.measureText(maxTextLengthCheese?.name + " id: " + maxTextLengthCheese?.id)
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

    private fun Canvas.drawBarcode(
        codeImage: Bitmap,
        label: String,
        verticalOffset: Float,
        horizontalOffset: Float
    ): Float {
        val baseline = -paintForText.ascent()

        val textW = paintForText.measureText(label).toInt()
        val textH = (baseline + paintForText.descent()).toInt()

        val maxWidth = max(codeImage.width, textW)

        val codeImageStart = maxWidth / 2F - codeImage.width / 2F
        val textImageStart = maxWidth / 2F - textW / 2F

        drawBitmap(codeImage, horizontalOffset + codeImageStart, verticalOffset, null)
        drawText(
            label,
            horizontalOffset + textImageStart,
            verticalOffset + baseline + codeImage.height,
            paintForText
        )

        return (textH + codeImage.height).toFloat()
    }

    override fun writeToStream(content: Any, stream: FileOutputStream) {
        if (content is PdfDocument)
            content.writeTo(stream)
        else throw IllegalArgumentException("Unsupported type")
    }
}