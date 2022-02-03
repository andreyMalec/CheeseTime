package com.malec.cheeselist.presentation.view

import android.widget.TextView
import androidx.databinding.BindingAdapter
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
}