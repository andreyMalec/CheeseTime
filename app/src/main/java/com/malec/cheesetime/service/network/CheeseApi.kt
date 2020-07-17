package com.malec.cheesetime.service.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseF
import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.filteredBy
import kotlinx.coroutines.tasks.await

class CheeseApi(private val db: FirebaseFirestore) {

    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    suspend fun getNextId(): Long {
        val data = db.collection("ids").document("next").get().await()
        val id = data.getLong("id")
        return id?.also {
            db.collection("ids").document("next").update("id", id + 1)
        } ?: 0
    }

    suspend fun getAll(): List<Cheese> {
        val data = db.collection("cheese").get().await()
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
        db.collection("cheese").add(cheese).await()
    }

    suspend fun update(cheese: Cheese) {
        val docId = getQueryById(cheese.id).documents[0]?.id
        db.collection("cheese").document(docId.toString()).update(cheese.toMap()).await()
    }

    suspend fun deleteById(id: Long) {
        val docId = getQueryById(id).documents[0]?.id
        db.collection("cheese").document(docId.toString()).delete()
    }

    private suspend fun getQueryById(id: Long) =
        db.collection("cheese").whereEqualTo("id", id).get().await()
}