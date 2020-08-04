package com.malec.cheesetime.model

data class Recipe(
    val id: Long,
    val name: String,
    val stages: String
) {
    fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "stages" to stages
    )
}

class RecipeF {
    val id: Long? = null
    val name: String = ""
    val stages: String = ""

    fun convert() =
        if (id == null)
            null
        else
            Recipe(id, name, stages)
}