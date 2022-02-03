package com.malec.domain.api

import com.google.firebase.firestore.ktx.toObject
import com.malec.domain.model.Task
import com.malec.domain.model.TaskF
import kotlinx.coroutines.tasks.await

class TaskApi(private val ref: ReferenceAllocator) {
    suspend fun getAll(): List<Task> {
        val data = ref.tasks().get().await()
        val tasks = data.documents.map { it.toObject<TaskF>()?.convert() }
        return tasks.filterNotNull()
    }

    suspend fun getNextId(): Long {
        val data = ref.nextTask().get().await()
        val id = data.getLong(ReferenceAllocator.ID)
        return id?.also {
            ref.nextTask().update(ReferenceAllocator.ID, id + 1)
        } ?: 1L
    }

    suspend fun getById(id: Long): Task? {
        val docs = getQueryById(id).documents
        return docs[0].toObject<TaskF>()?.convert()
    }

    suspend fun create(task: Task) {
        ref.tasks().add(task).await()
    }

    suspend fun update(task: Task) {
        val docId = getQueryById(task.id).documents[0]?.id.toString()
        ref.tasks().document(docId).update(task.toMap()).await()
    }

    private suspend fun getQueryById(id: Long) =
        ref.tasks().whereEqualTo(ReferenceAllocator.ID, id).get().await()
}