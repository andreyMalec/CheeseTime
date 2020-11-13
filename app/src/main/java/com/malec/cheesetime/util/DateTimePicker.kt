package com.malec.cheesetime.util

import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.malec.cheesetime.R
import java.util.*

class DateTimePicker(private val activity: FragmentActivity) {
    fun pickDate(onDatePicked: (String) -> Unit) {
        getDatePicker().also {
            it.addOnPositiveButtonClickListener { date ->
                onDatePicked(DateFormatter.simpleFormat(date))
            }
        }.show(activity.supportFragmentManager, "datePicker")
    }

    fun pickTime(onTimePicked: (time: String) -> Unit) {
        getTimePicker(onTimePicked).show()
    }

    private fun getTimePicker(onTimePicked: (time: String) -> Unit): TimePickerDialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity,
            R.style.AppTheme_TimePicker,
            { v, _, _ ->
                onTimePicked(v.hour.toString() + ":" + v.minute)
            }, hour, minute, DateFormat.is24HourFormat(activity)
        )
    }

    private fun getDatePicker() = MaterialDatePicker.Builder.datePicker().apply {
        setTheme(R.style.AppTheme_DatePicker)
    }.build()
}