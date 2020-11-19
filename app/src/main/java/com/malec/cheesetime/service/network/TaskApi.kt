package com.malec.cheesetime.service.network

import com.google.firebase.firestore.ktx.toObject
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.model.TaskF
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
        } ?: 0
    }

    suspend fun getById(id: Long): Task? {
        val docs = getQueryById(id).documents
        return docs[0].toObject<TaskF>()?.convert()
    }

    suspend fun create(task: Task) {
        ref.tasks().add(task).await()
    }

    suspend fun update(task: Task) {
        val docId = getQueryById(task.id).documents[0]?.id
        ref.tasks().document(docId.toString()).update(task.toMap()).await()
    }

    suspend fun deleteById(id: Long) {
        val docId = getQueryById(id).documents[0]?.id
        ref.tasks().document(docId.toString()).delete()
    }

    private suspend fun getQueryById(id: Long) =
        ref.tasks().whereEqualTo(ReferenceAllocator.ID, id).get().await()
}