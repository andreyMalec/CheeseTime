package com.malec.cheeselist.dependencies

interface CheeseListOutput {
    fun openDetail(id: Long)

    fun onClickExit()
}