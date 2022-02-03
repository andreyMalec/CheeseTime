package com.malec.domain.model

class CheeseF : DTO() {
    val id: Long = 0
    val name: String = ""
    val dateStart: Long = 0
    val date: Long = 0
    val recipe: String = ""
    val comment: String = ""
    val milk: String = ""
    val composition: String = ""
    val stages: String = ""
    val volume: String = ""
    val volumeMax: String = ""
    val badgeColor: Int = 0
    val archived: Boolean = false
    val photo: String = ""
    val deleted: Boolean = false
    val init: Void? = null

    fun convert(): Cheese? {
        if (id == 0L)
            return null

        val milkParams = milk.split(separator)
        return Cheese(
            id,
            name,
            dateStart,
            date,
            recipe,
            comment,
            milkParams[0],
            milkParams[1],
            milkParams[2],
            composition,
            stages.split(separator),
            volume,
            volumeMax,
            badgeColor,
            false,
            archived,
            photo.split(separator),
            deleted
        )
    }
}