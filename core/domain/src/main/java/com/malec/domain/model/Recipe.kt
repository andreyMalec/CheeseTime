package com.malec.domain.model

data class Recipe(
    val id: Long,
    val name: String,
    val stages: List<String>
) : DTO() {
    fun toMap() = mapOf(
        ID to id,
        NAME to name,
        STAGES to stages.joinToString(separator)
    )
}