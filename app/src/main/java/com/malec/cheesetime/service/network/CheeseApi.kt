package com.malec.cheesetime.service.network

import com.google.firebase.firestore.ktx.toObject
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseF
import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.filteredBy
import kotlinx.coroutines.tasks.await

class CheeseApi(private val ref: ReferenceAllocator) {
    suspend fun getNextId(): Long {
        val data = ref.nextCheese().get().await()
        val id = data.getLong("id")
        return id?.also {
            ref.nextCheese().update("id", id + 1)
        } ?: 0
    }

    suspend fun getAll(): List<Cheese> {
        val data = ref.cheeses().get().await()
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
        ref.cheeses().add(cheese.toMap()).await()
    }

    suspend fun update(cheese: Cheese) {
        val docId = getQueryById(cheese.id).documents[0]?.id
        ref.cheeses().document(docId.toString()).update(cheese.toMap()).await()
    }

    suspend fun deleteById(id: Long) {
        val docId = getQueryById(id).documents[0]?.id
        ref.cheeses().document(docId.toString()).delete()
    }

    private suspend fun getQueryById(id: Long) =
        ref.cheeses().whereEqualTo("id", id).get().await()
}