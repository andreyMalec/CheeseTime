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
        id: Long? = null
    ): Cheese {
        val timeId = id ?: Calendar.getInstance().timeInMillis

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dateM = format.parse(date)?.time ?: 0

        return Cheese(
            timeId,
            name,
            dateM,
            recipe,
            comment ?: "",
            "$milkType♂$milkVolume♂$milkAge",
            composition ?: "",
            stages?.joinToString("♂") ?: "",
            badgeColor ?: 0
        )
    }
}