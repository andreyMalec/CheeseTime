package com.malec.domain.model

class TaskF {
    val id: Long? = null
    val cheeseId: Long = 0
    val cheeseName: String = ""
    val todo: String = ""
    val date: Long = 0
    val comment: String = ""
    val deleted: Boolean = false
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
                comment,
                deleted
            )
}