package com.malec.cheesetime.service.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.model.TaskF
import kotlinx.coroutines.tasks.await

class TaskApi(private val db: FirebaseFirestore, auth: FirebaseAuth) {
    private val userId = auth.currentUser?.uid

    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    suspend fun getAll(): List<Task> {
        val data = db.collection("$userId|task").get().await()
        val tasks = data.documents.map { it.toObject<TaskF>()?.convert() }
        return tasks.filterNotNull()
    }

    suspend fun getNextId(): Long {
        val data = db.collection("$userId|ids").document("nextTask").get().await()
        val id = data.getLong("id")
        return id?.also {
            db.collection("$userId|ids").document("nextTask").update("id", id + 1)
        } ?: 0
    }

    suspend fun getById(id: Long): Task? {
        val docs = getQueryById(id).documents
        return docs[0].toObject<TaskF>()?.convert()
    }

    suspend fun create(task: Task) {
        db.collection("$userId|task").add(task).await()
    }

    suspend fun update(task: Task) {
        val docId = getQueryById(task.id).documents[0]?.id
        db.collection("$userId|task").document(docId.toString()).update(task.toMap()).await()
    }

    suspend fun deleteById(id: Long) {
        val docId = getQueryById(id).documents[0]?.id
        db.collection("$userId|task").document(docId.toString()).delete()
    }

    private suspend fun getQueryById(id: Long) =
        db.collection("$userId|task").whereEqualTo("id", id).get().await()
}