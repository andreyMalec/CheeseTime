package com.malec.cheesetime.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.storage.StorageReference
import com.malec.cheesetime.di.module.GlideApp
import com.malec.cheesetime.util.DateFormatter
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["bind:from", "bind:to"])
    fun date(textView: TextView, dateFrom: Long, dateTo: Long) {
        val format = SimpleDateFormat("dd.MM.yy", Locale.ENGLISH)
        val dateFromText = format.format(dateFrom)
        val dateToText = format.format(dateTo)
        val dateText = "($dateFromText - $dateToText)"
        textView.text = dateText
    }

    @JvmStatic
    @BindingAdapter("dateFormatted")
    fun dateFormatted(textView: TextView, dateFormatted: Long) {
        textView.text = DateFormatter(textView.context).format(dateFormatted)
    }

    @JvmStatic
    @BindingAdapter("storageRef")
    fun storageRef(imageView: ImageView, storageRef: StorageReference?) {
        if (storageRef != null)
            GlideApp.with(imageView.context).load(storageRef).into(imageView)
    }
}