package com.malec.cheesetime.ui

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.firebase.storage.StorageReference
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.malec.cheesetime.di.module.GlideApp
import com.malec.cheesetime.util.DateFormatter
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["from", "to"])
    fun date(textView: TextView, dateFrom: Long, dateTo: Long) {
        val format = SimpleDateFormat("dd.MM.yy", Locale.ENGLISH)
        val dateFromText = format.format(dateFrom)
        val dateToText = format.format(dateTo)
        val dateText = "$dateFromText - $dateToText"
        textView.text = dateText
    }

    @JvmStatic
    @BindingAdapter("dateFormatted")
    fun dateFormatted(textView: TextView, dateFormatted: Long) {
        textView.text = DateFormatter(textView.context).format(dateFormatted)
    }

    @JvmStatic
    @BindingAdapter("dateSimpleFormat")
    fun dateSimpleFormat(textView: TextView, dateSimpleFormat: Long) {
        textView.text = DateFormatter.simpleFormat(dateSimpleFormat)
    }

    @JvmStatic
    @BindingAdapter("storageRef")
    fun storageRef(imageView: ImageView, storageRef: StorageReference?) {
        if (storageRef != null)
            GlideApp.with(imageView.context).load(storageRef).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("bitmap")
    fun bitmap(imageView: ImageView, bitmap: Bitmap?) {
        if (bitmap != null)
            imageView.setImageBitmap(bitmap)
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