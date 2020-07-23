package com.malec.cheesetime.model

data class Cheese(
    val id: Long,
    val name: String,
    val dateStart: Long,
    val date: Long,
    val recipe: String,
    val comment: String,
    val milk: String,
    val composition: String,
    val stages: String,
    val badgeColor: Int,
    val isSelected: Boolean,
    val isArchived: Boolean
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
        "badgeColor" to badgeColor,
        "isArchived" to isArchived
    )

    fun toggleSelect() = Cheese(
        id,
        name,
        dateStart,
        date,
        recipe,
        comment,
        milk,
        composition,
        stages,
        badgeColor,
        !isSelected,
        isArchived
    )

    fun toggleArchive() = Cheese(
        id,
        name,
        dateStart,
        date,
        recipe,
        comment,
        milk,
        composition,
        stages,
        badgeColor,
        isSelected,
        !isArchived
    )
}

class CheeseF {
    val id: Long? = null
    val name: String = ""
    val dateStart: Long = 0
    val date: Long = 0
    val recipe: String = ""
    val comment: String = ""
    val milk: String = ""
    val composition: String = ""
    val stages: String = ""
    val badgeColor: Int = 0
    val isArchived: Boolean = false

    fun convert() =
        if (id == null)
            null
        else
            Cheese(
                id,
                name,
                dateStart,
                date,
                recipe,
                comment,
                milk,
                composition,
                stages,
                badgeColor,
                false,
                isArchived
            )
}