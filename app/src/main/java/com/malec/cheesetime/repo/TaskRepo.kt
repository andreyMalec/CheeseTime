package com.malec.cheesetime.repo

import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.service.network.CheeseApi
import com.malec.cheesetime.service.network.TaskApi
import com.malec.cheesetime.service.notifications.TaskScheduler
import kotlinx.coroutines.delay

class TaskRepo(
    private val api: TaskApi,
    private val cheeseApi: CheeseApi,
    private val scheduler: TaskScheduler
) {

    suspend fun getAll() = api.getAll().sortedByDescending { it.date }

    suspend fun scheduleAll(tasks: List<Task>) {
        tasks.forEach {
            scheduler.schedule(it)
            delay(200)
        }
    }

    suspend fun getCheeseList() =
        cheeseApi.getAllFiltered(CheeseFilter())

    suspend fun getNextId() = try {
        api.getNextId()
    } catch (e: Exception) {
        e.printStackTrace()
        1L
    }

    suspend fun getById(id: Long) = api.getById(id)

    suspend fun create(task: Task) = api.create(task)

    suspend fun update(task: Task) = api.update(task)

    suspend fun deleteById(id: Long) = api.deleteById(id)
}