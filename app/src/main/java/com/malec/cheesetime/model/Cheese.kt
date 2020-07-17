package com.malec.cheesetime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cheese(
    @PrimaryKey
    val id: Long,
    val name: String,
    val dateStart: Long,
    val date: Long,
    val recipe: String,
    val comment: String,
    val milk: String,
    val composition: String,
    val stages: String,
    val badgeColor: Int
) {
    fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "dateStart" to dateStart,
        "date" to date,
        "recipe" to recipe,
        "comment" to comment,
        "milk" to milk,
        "composition" to composition,
        "stages" to stages,
        "badgeColor" to badgeColor
    )
}

class CheeseF {
    val id: Long = 0
    val name: String = ""
    val dateStart: Long = 0
    val date: Long = 0
    val recipe: String = ""
    val comment: String = ""
    val milk: String = ""
    val composition: String = ""
    val stages: String = ""
    val badgeColor: Int = 0

    fun convert() =
        Cheese(id, name, dateStart, date, recipe, comment, milk, composition, stages, badgeColor)
}