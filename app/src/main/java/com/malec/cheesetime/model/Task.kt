package com.malec.cheesetime.model

import java.util.*

data class Task(
    var id: Long,
    var cheeseId: Long,
    var cheeseName: String,
    var todo: String,
    var date: Long,
    var comment: String
) {
    fun toMap() = mapOf(
        "id" to id,
        "cheeseId" to cheeseId,
        "cheeseName" to cheeseName,
        "todo" to todo,
        "date" to date,
        "comment" to comment
    )

    companion object {
        fun empty(): Task {
            val now = Date().time

            return Task(
                0,
                0,
                "",
                "",
                now,
                ""
            )
        }
    }
}

class TaskF {
    val id: Long? = null
    val cheeseId: Long = 0
    val cheeseName: String = ""
    val todo: String = ""
    val date: Long = 0
    val comment: String = ""
    val init: Void? = null

    fun convert() =
        if (id == null)
            null
        else
            Task(
                id,
                cheeseId,
                cheeseName,
                todo,
                date,
                comment
            )
}