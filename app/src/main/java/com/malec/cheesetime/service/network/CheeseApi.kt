package com.malec.cheesetime.service.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseF
import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.filteredBy
import kotlinx.coroutines.tasks.await

class CheeseApi(private val db: FirebaseFirestore) {
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid

    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    suspend fun getNextId(): Long {
        val data = db.collection("$userId|ids").document("nextCheese").get().await()
        val id = data.getLong("id")
        return id?.also {
            db.collection("$userId|ids").document("nextCheese").update("id", id + 1)
        } ?: 0
    }

    suspend fun getAll(): List<Cheese> {
        val data = db.collection("$userId|cheese").get().await()
        val cheeses = data.documents.map { it.toObject<CheeseF>()?.convert() }
        return cheeses.filterNotNull()
    }

    suspend fun getAllFiltered(filter: CheeseFilter): List<Cheese> {
        return getAll().filteredBy(filter)
    }

    suspend fun getById(id: Long): Cheese? {
        val docs = getQueryById(id).documents
        return docs[0].toObject<CheeseF>()?.convert()
    }

    suspend fun create(cheese: Cheese) {
        db.collection("$userId|cheese").add(cheese).await()
    }

    suspend fun update(cheese: Cheese) {
        val docId = getQueryById(cheese.id).documents[0]?.id
        db.collection("$userId|cheese").document(docId.toString()).update(cheese.toMap()).await()
    }

    suspend fun deleteById(id: Long) {
        val docId = getQueryById(id).documents[0]?.id
        db.collection("$userId|cheese").document(docId.toString()).delete()
    }

    private suspend fun getQueryById(id: Long) =
        db.collection("$userId|cheese").whereEqualTo("id", id).get().await()
}