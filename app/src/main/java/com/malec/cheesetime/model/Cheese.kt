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
    var stages: List<String>,
    var badgeColor: Int,
    var isSelected: Boolean,
    var isArchived: Boolean,
    var photos: List<String>
) : DTO() {
    companion object {
        fun empty() = Cheese(0)
    }

    constructor(
        id: Long,
        name: String? = null,
        dateStart: Long? = null,
        date: Long? = null,
        recipe: String? = null,
        comment: String? = null,
        milkType: String? = null,
        milkVolume: String? = null,
        milkAge: String? = null,
        composition: String? = null,
        stages: List<String>? = null,
        badgeColor: Int? = null,
        isArchived: Boolean? = null,
        photos: List<String>? = null
    ) : this(
        id,
        name ?: "",
        dateStart ?: System.currentTimeMillis(),
        date ?: System.currentTimeMillis(),
        recipe ?: "",
        comment ?: "",
        milkType ?: "",
        milkVolume ?: "",
        milkAge ?: "",
        composition ?: "",
        stages ?: listOf(),
        badgeColor ?: 0,
        false,
        isArchived ?: false,
        photos ?: listOf()
    )

    fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "dateStart" to dateStart,
        "date" to date,
        "recipe" to recipe,
        "comment" to comment,
        "milk" to "$milkType$separator$milkVolume$separator$milkAge",
        "composition" to composition,
        "stages" to stages.joinToString(separator),
        "badgeColor" to badgeColor,
        "archived" to isArchived,
        "photo" to photos.joinToString(separator)
    )

    fun toggleSelect() {
        isSelected = !isSelected
    }

    fun toggleArchive() {
        isArchived = !isArchived
    }
}

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
    val badgeColor: Int = 0
    val archived: Boolean = false
    val photo: String = ""
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
            badgeColor,
            false,
            archived,
            photo.split(separator)
        )
    }
}