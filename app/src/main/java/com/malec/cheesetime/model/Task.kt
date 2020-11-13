package com.malec.cheesetime.model

data class Task(
    val id: Long,
    val cheeseId: Long,
    val cheeseName: String,
    val todo: String,
    val date: Long,
    val comment: String
) {
    fun toMap() = mapOf(
        "id" to id,
        "cheeseId" to cheeseId,
        "cheeseName" to cheeseName,
        "todo" to todo,
        "date" to date,
        "comment" to comment
    )
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