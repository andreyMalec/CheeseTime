package com.malec.tasklist.presentation.view

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.malec.domain.util.DateFormatter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("dateFormatted")
    fun dateFormatted(textView: TextView, dateFormatted: Long) {
        textView.text = DateFormatter(textView.context).format(dateFormatted)
    }
}