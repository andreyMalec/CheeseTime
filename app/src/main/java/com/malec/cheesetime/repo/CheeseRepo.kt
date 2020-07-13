package com.malec.cheesetime.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.malec.cheesetime.model.Cheese
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.util.*

@ExperimentalCoroutinesApi
class CheeseRepo {
    private val db = FirebaseFirestore.getInstance()

    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    fun getAll(): Flow<List<Cheese>> = TODO()

    fun getById(id: Long): Flow<Cheese> = TODO()

    fun create(
        name: String,
        date: Long,
        recipe: String,
        comment: String?,
        milk: String,
        composition: String?,
        stages: String?,
        badgeColor: Int?,
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        val time = Calendar.getInstance().timeInMillis

        val newCheese = Cheese(
            time,
            name,
            date,
            recipe,
            comment ?: "",
            milk,
            composition ?: "",
            stages ?: "",
            badgeColor ?: 0
        )
        db.collection("cheese").add(newCheese)
            .addOnCompleteListener(dataCompleteListener(onSuccess, onFailure))
    }

    fun update(
        name: String,
        date: Long,
        recipe: String,
        comment: String?,
        milk: String,
        composition: String?,
        stages: String?,
        badgeColor: Int?,
        cheese: Cheese
    ) {
        val updatedCheese = Cheese(
            cheese.id,
            name,
            date,
            recipe,
            comment ?: "",
            milk,
            composition ?: "",
            stages ?: "",
            badgeColor ?: 0
        )
        db.collection("cheese").whereEqualTo("id", cheese.id).get().addOnCompleteListener {
            val docId = it.result?.documents?.get(0)?.id
            db.collection("cheese").document(docId.toString()).update(updatedCheese.toMap())
        }
    }

    fun deleteById(id: Long) {
        db.collection("cheese").whereEqualTo("id", id).get().addOnCompleteListener {
            val docId = it.result?.documents?.get(0)?.id
            db.collection("cheese").document(docId.toString()).delete()
        }
    }
}