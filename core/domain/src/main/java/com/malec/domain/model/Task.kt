package com.malec.domain.model

data class Task(
    var id: Long,
    var cheeseId: Long,
    var cheeseName: String,
    var todo: String,
    var date: Long,
    var comment: String,
    var isDeleted: Boolean
) : DTO() {
    constructor(
        id: Long,
        cheeseId: Long? = null,
        cheeseName: String? = null,
        todo: String? = null,
        date: Long? = null,
        comment: String? = null,
        isDeleted: Boolean? = null
    ) : this(
        id,
        cheeseId ?: 0,
        cheeseName ?: "",
        todo ?: "",
        date ?: System.currentTimeMillis(),
        comment ?: "",
        isDeleted ?: false
    )

    fun toMap() = mapOf(
        ID to id,
        CHEESE_ID to cheeseId,
        CHEESE_NAME to cheeseName,
        TODO to todo,
        DATE to date,
        COMMENT to comment,
        DELETED to isDeleted
    )

    fun delete() = copy(
        isDeleted = true
    )

    companion object {
        fun empty() = Task(0)

        private const val CHEESE_ID = "cheeseId"
        private const val CHEESE_NAME = "cheeseName"
        private const val TODO = "todo"
    }
}