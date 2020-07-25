package com.malec.cheesetime.util

import com.malec.cheesetime.model.Cheese
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

        val dateM = DateFormatter.dateFromString(date)

        val stagesFiltered = stages?.filter {
            !it.isNullOrBlank()
        }

        return Cheese(
            id,
            name,
            dateStart,
            dateM,
            recipe,
            comment ?: "",
            "$milkType♂$milkVolume♂$milkAge",
            composition ?: "",
            stagesFiltered?.joinToString("♂") ?: "",
            badgeColor ?: 0,
            false,
            isArchived ?: false
        )
    }
}