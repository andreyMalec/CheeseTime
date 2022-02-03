package com.malec.cheesedetail.presentation.view

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.malec.domain.util.DateFormatter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("dateSimpleFormat")
    fun dateSimpleFormat(textView: TextView, dateSimpleFormat: Long) {
        textView.text = DateFormatter.simpleFormat(dateSimpleFormat)
    }

    @JvmStatic
    @BindingAdapter("barcode")
    fun barcode(barcodeImage: ImageView, barcode: Long?) {
        if (barcode != null) {
            val barcodeBitmap =
                BarcodeEncoder().encodeBitmap(
                    barcode.toString(),
                    BarcodeFormat.CODE_128,
                    550,
                    100
                )
            barcodeImage.setImageBitmap(barcodeBitmap)
            barcodeImage.isVisible = true
        }
    }
}