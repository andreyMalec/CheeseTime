package com.malec.cheesetime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cheese(
    @PrimaryKey
    val id: Long,
    val name: String,
    val date: Long,
    val recipe: String,
    val comment: String,
    val milk: String,
    val composition: String,
    val stages: String,
    val badgeColor: Int
)