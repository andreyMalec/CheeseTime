package com.malec.domain.model

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
    var volume: String,
    var volumeMax: String,
    var badgeColor: Int,
    var isSelected: Boolean,
    var isArchived: Boolean,
    var photos: List<String>,
    var isDeleted: Boolean
) : DTO() {
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
        volume: String? = null,
        volumeMax: String? = null,
        badgeColor: Int? = null,
        isArchived: Boolean? = null,
        photos: List<String>? = null,
        isDeleted: Boolean? = null
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
        volume ?: "",
        volumeMax ?: "",
        badgeColor ?: 0,
        false,
        isArchived ?: false,
        photos ?: listOf(),
        isDeleted ?: false
    )

    fun toMap() = mapOf(
        ID to id,
        NAME to name,
        DATE_START to dateStart,
        DATE to date,
        RECIPE to recipe,
        COMMENT to comment,
        MILK to "$milkType$separator$milkVolume$separator$milkAge",
        COMPOSITION to composition,
        STAGES to stages.joinToString(separator),
        VOLUME to volume,
        VOLUME_MAX to volumeMax,
        BADGE_COLOR to badgeColor,
        IS_ARCHIVED to isArchived,
        PHOTOS to photos.joinToString(separator),
        DELETED to isDeleted
    )

    fun toggleSelect() = copy(
        isSelected = !isSelected
    )

    fun toggleArchive() = copy(
        isArchived = !isArchived
    )

    fun archive() = copy(
        isArchived = true
    )

    fun delete() = copy(
        isDeleted = true
    )

    companion object {
        fun empty() = Cheese(0)

        private const val DATE_START = "dateStart"
        private const val RECIPE = "recipe"
        private const val MILK = "milk"
        private const val COMPOSITION = "composition"
        private const val VOLUME = "volume"
        private const val VOLUME_MAX = "volumeMax"
        private const val BADGE_COLOR = "badgeColor"
        private const val IS_ARCHIVED = "archived"
        private const val PHOTOS = "photo"
    }
}