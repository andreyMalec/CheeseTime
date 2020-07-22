package com.malec.cheesetime.repo

import com.malec.cheesetime.service.network.TaskApi

class TaskRepo(private val api: TaskApi) {

    suspend fun getAll() = api.getAll()
}