package com.malec.cheesetime.util

import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Photo
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
        photo: List<Photo>?,
        id: Long,
        dateStart: Long? = null
    ): Cheese {
        val now = Date().time

        val dateM = DateFormatter.dateFromString(date)

        val stagesFiltered = stages?.filter {
            !it.isBlank()
        }

        val milk = makeString(milkType, milkVolume, milkAge)

        return Cheese(
            id,
            name,
            dateStart ?: now,
            dateM,
            recipe,
            comment ?: "",
            milk,
            composition ?: "",
            stagesFiltered.makeString(),
            badgeColor ?: 0,
            false,
            isArchived ?: false,
            photo?.map {
                it.name
            }.makeString()
        )
    }

    private fun makeString(vararg values: Any): String {
        var s = ""
        for (value in values)
            s += "$value♂"
        return s.dropLast(1)
    }

    private fun List<Any>?.makeString() = this?.joinToString("♂") ?: ""
}