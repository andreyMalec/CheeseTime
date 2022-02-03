package com.malec.tasklist.dependencies

interface TaskListOutput {
    fun openDetail(id: Long)

    fun onClickExit()
}