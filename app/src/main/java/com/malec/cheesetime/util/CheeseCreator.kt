package com.malec.cheesetime.util

import com.malec.cheesetime.model.Cheese
import java.text.SimpleDateFormat
import java.util.*

object CheeseCreator {
    fun create(
        name: String,
        date: String,
        recipe: String,
        comment: String?,
        milkType: String,
        milkVolume: String,
        milkAge: String,
        composition: String?,
        stages: List<String>?,
        badgeColor: Int?,
        isArchived: Boolean?,
        id: Long
    ): Cheese {
        val dateStart = Date().time

        val dateM = dateFromString(date)

        return Cheese(
            id,
            name,
            dateStart,
            dateM,
            recipe,
            comment ?: "",
            "$milkType♂$milkVolume♂$milkAge",
            composition ?: "",
            stages?.joinToString("♂") ?: "",
            badgeColor ?: 0,
            false,
            isArchived ?: false
        )
    }

    fun dateFromString(date: String): Long {
        val d = date.split("/")
        val pattern =
            "d".repeat(d[0].length) + "/" + "M".repeat(d[1].length) + "/" + "y".repeat(d[2].length)
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        return format.parse(date)?.time ?: 0
    }

    fun isDateValid(date: String): Boolean {
        val regex =
            Regex("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$")
        return date.matches(regex)
    }
}