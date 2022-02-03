package com.malec.domain.repository

import com.malec.domain.api.TaskApi
import com.malec.domain.model.Task
import kotlinx.coroutines.delay

class TaskRepo(
    private val api: TaskApi,
    private val scheduler: TaskScheduler
) {
    suspend fun getAll() = api.getAll()
        .filter { !it.isDeleted }
        .sortedByDescending { it.date }

    suspend fun scheduleAll(tasks: List<Task>) {
        tasks.forEach {
            scheduler.schedule(it)
            delay(200)
        }
    }

    suspend fun getById(id: Long) = api.getById(id)

    suspend fun save(task: Task) {
        if (task.id == 0L)
            api.create(
                task.copy(id = getNextId())
            )
        else
            api.update(task)
    }

    private suspend fun getNextId() = api.getNextId()

    suspend fun delete(task: Task) {
        scheduler.cancel(task.id)
        api.update(task.delete())
    }
}