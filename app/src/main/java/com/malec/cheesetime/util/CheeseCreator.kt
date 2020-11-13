package com.malec.cheesetime.util

import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Photo
import java.util.*

object CheeseCreator {
    fun create(
        name: String?,
        date: Long?,
        recipe: String?,
        comment: String?,
        milkType: String?,
        milkVolume: String?,
        milkAge: String?,
        composition: String?,
        stages: List<String>?,
        badgeColor: Int?,
        isArchived: Boolean?,
        photo: List<Photo>?,
        id: Long,
        dateStart: Long? = null
    ): Cheese {
        val now = Date().time

        val stagesFiltered = stages?.filter {
            !it.isBlank()
        }

        return Cheese(
            id,
            name.takeIf { !it.isNullOrBlank() } ?: recipe ?: "",
            dateStart ?: now,
            date ?: now,
            recipe ?: "",
            comment ?: "",
            milkType ?: "",
            milkVolume ?: "",
            milkAge.toString(),
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

    fun empty(): Cheese {
        val now = Date().time
        return Cheese(
            0,
            "",
            now,
            now,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0,
            false,
            false,
            ""
        )
    }

    private fun makeString(vararg values: Any?) = values.joinToString("♂")

    private fun List<Any>?.makeString() = this?.joinToString("♂") ?: ""
}