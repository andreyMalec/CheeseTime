package com.malec.cheesetime.repo

import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.service.network.CheeseApi
import com.malec.cheesetime.service.network.TaskApi

class TaskRepo(private val api: TaskApi, private val cheeseApi: CheeseApi) {

    suspend fun getAll() = api.getAll()

    suspend fun getCheeseList() =
        cheeseApi.getAllFiltered(CheeseFilter())//TODO фильтр для не архивных

    suspend fun getNextId() = api.getNextId()

    suspend fun getById(id: Long) = api.getById(id)

    suspend fun create(task: Task) = api.create(task)

    suspend fun update(task: Task) = api.update(task)

    suspend fun deleteById(id: Long) = api.deleteById(id)
}