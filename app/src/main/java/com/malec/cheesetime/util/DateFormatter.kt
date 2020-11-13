package com.malec.cheesetime.util

import android.content.Context
import com.malec.cheesetime.R
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter(private val context: Context) {
    fun format(date: Long): String {
        val now = Date().time
        val diff = date - now
        val diffDays = diff / millisecondsInDay

        return when {
            diffDays <= 1 -> formatHours(diff)
            diffDays > 1 && diff < 2 -> context.getString(R.string.date_yesterday)
            else -> simpleFormat(date)
        }
    }

    private fun formatHours(diff: Long) =
        when (val diffHours = diff / millisecondsInHour) {
            in 0..1 -> formatMinutes(diff)
            in 2..23 -> {
                if (diffHours in 2..4 || diffHours in 22..23)
                    context.getString(R.string.date_in_n_hours, diffHours)
                else if (diffHours in 5..20)
                    context.getString(R.string.date_in_n_hours2, diffHours)
                else context.getString(R.string.date_in_n_hours3, diffHours)
            }
            else -> context.getString(R.string.date_expired)
        }

    private fun formatMinutes(diff: Long) =
        when (val diffMinutes = diff / millisecondsInMinute) {
            in 2..59 -> {
                if (diffMinutes in 2..4 || diffMinutes in 22..24 || diffMinutes in 22..24
                    || diffMinutes in 32..34 || diffMinutes in 42..44 || diffMinutes in 52..54
                )
                    context.getString(
                        R.string.date_in_n_minutes,
                        diffMinutes
                    )
                else if (diffMinutes in 5..20 || diffMinutes in 25..30 || diffMinutes in 35..40
                    || diffMinutes in 45..50 || diffMinutes in 55..60
                )
                    context.getString(
                        R.string.date_in_n_minutes2,
                        diffMinutes
                    )
                else context.getString(
                    R.string.date_in_n_minutes3,
                    diffMinutes
                )
            }
            else -> context.getString(R.string.date_now)
        }

    companion object {
        private const val millisecondsInMinute: Long = 1000 * 60
        const val millisecondsIn30Min: Long = millisecondsInMinute * 30
        const val millisecondsInHour: Long = millisecondsIn30Min * 2
        private const val millisecondsInDay: Long = millisecondsInHour * 24

        fun dateTimeFromString(dateString: String, timeString: String): Long {
            val time = timeFromString(timeString)
            val timeInMillis = time.first * millisecondsInHour + time.second * millisecondsInMinute
            return dateFromString(dateString) + timeInMillis
        }

        fun timeFromString(time: String): Pair<Int, Int> {
            val h = time.split(":")[0].toInt()
            val m = time.split(":")[1].toInt()
            return Pair(h, m)
        }

        fun dateFromString(date: String?): Long {
            if (date.isNullOrBlank()) return Date().time

            val mDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).parse(date) ?: Date()
            return mDate.time
        }

        fun simpleFormatTime(date: Long) =
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)

        fun simpleFormat(date: Long) =
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
    }
}