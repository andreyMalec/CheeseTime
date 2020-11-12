package com.malec.cheesetime.model

data class Cheese(
    var id: Long,
    var name: String,
    var dateStart: Long,
    var date: Long,
    var recipe: String,
    var comment: String,
    var milkType: String,
    var milkVolume: String,
    var milkAge: String,
    var composition: String,
    var stages: String,
    var badgeColor: Int,
    var isSelected: Boolean,
    var isArchived: Boolean,
    var photo: String
) {
    fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "dateStart" to dateStart,
        "date" to date,
        "recipe" to recipe,
        "comment" to comment,
        "milk" to "$milkType♂$milkVolume♂$milkAge",
        "composition" to composition,
        "stages" to stages,
        "badgeColor" to badgeColor,
        "archived" to isArchived,
        "photo" to photo
    )

    fun toggleSelect() = Cheese(
        id,
        name,
        dateStart,
        date,
        recipe,
        comment,
        milkType,
        milkVolume,
        milkAge,
        composition,
        stages,
        badgeColor,
        !isSelected,
        isArchived,
        photo
    )

    fun toggleArchive() = Cheese(
        id,
        name,
        dateStart,
        date,
        recipe,
        comment,
        milkType,
        milkVolume,
        milkAge,
        composition,
        stages,
        badgeColor,
        isSelected,
        !isArchived,
        photo
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
    val archived: Boolean = false
    val photo: String = ""

    fun convert() =
        if (id == null)
            null
        else {
            val milkParams = milk.split("♂")
            Cheese(
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
                stages,
                badgeColor,
                false,
                archived,
                photo
            )
        }
}