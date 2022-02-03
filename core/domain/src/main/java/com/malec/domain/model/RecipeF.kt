package com.malec.domain.model

class RecipeF : DTO() {
    val id: Long = 0
    val name: String = ""
    val stages: String = ""
    val init: Void? = null

    fun convert() = if (id == 0L) null else Recipe(id, name, stages.split(separator))
}