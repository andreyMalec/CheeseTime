package com.malec.cheesetime.model

data class Recipe(
    val id: Long,
    val name: String,
    val stages: List<String>
) : DTO() {
    fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "stages" to stages.joinToString(separator)
    )
}

class RecipeF : DTO() {
    val id: Long = 0
    val name: String = ""
    val stages: String = ""
    val init: Void? = null

    fun convert() = if (id == 0L) null else Recipe(id, name, stages.split(separator))
}