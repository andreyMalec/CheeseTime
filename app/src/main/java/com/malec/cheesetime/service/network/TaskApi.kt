package com.malec.cheesetime.service.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.model.TaskF
import kotlinx.coroutines.tasks.await

class TaskApi(private val db: FirebaseFirestore) {

    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    suspend fun getAll(): List<Task> {
        val data = db.collection("task").get().await()
        val tasks = data.documents.map { it.toObject<TaskF>()?.convert() }
        return tasks.filterNotNull()
    }

    suspend fun getNextId(): Long {
        val data = db.collection("ids").document("nextTask").get().await()
        val id = data.getLong("id")
        return id?.also {
            db.collection("ids").document("nextTask").update("id", id + 1)
        } ?: 0
    }
}