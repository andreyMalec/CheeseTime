package com.malec.presentation.dialog

import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.malec.domain.util.DateFormatter
import com.malec.presentation.R
import java.util.*

class DateTimePicker(private val context: Context, private val fragmentManager: FragmentManager) {
    fun pickDate(onDatePicked: (String) -> Unit) {
        getDatePicker().also {
            it.addOnPositiveButtonClickListener { date ->
                onDatePicked(DateFormatter.simpleFormat(date))
            }
        }.show(fragmentManager, "datePicker")
    }

    fun pickTime(onTimePicked: (time: String) -> Unit) {
        getTimePicker(onTimePicked).show()
    }

    private fun getTimePicker(onTimePicked: (time: String) -> Unit): TimePickerDialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            context,
            R.style.AppTheme_TimePicker,
            { _, h, m ->
                onTimePicked(DateFormatter.simpleFormatTime(h, m))
            }, hour, minute, DateFormat.is24HourFormat(context)
        )
    }

    private fun getDatePicker() = MaterialDatePicker.Builder.datePicker().apply {
        setTheme(R.style.AppTheme_DatePicker)
        this.setCalendarConstraints(
            CalendarConstraints.Builder().setStart(System.currentTimeMillis()).build()
        )
    }.build()
}