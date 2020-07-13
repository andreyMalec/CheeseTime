package com.malec.cheesetime.repo

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseF
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    fun getAll(
        onSuccess: (cheeses: List<Cheese>) -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        db.collection("cheese").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result?.documents
                if (result != null) {
                    val cheeses = result.map { doc ->
                        doc.toObject(CheeseF::class.java)?.convert()
                    }
                    onSuccess(cheeses.filterNotNull())

                    return@addOnCompleteListener
                }
            }

            onFailure(it.exception)
        }
    }

    fun getById(
        id: Long,
        onSuccess: (cheese: Cheese) -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        db.collection("cheese").whereEqualTo("id", id).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result?.documents
                if (result != null) {
                    val cheese = result[0].toObject(CheeseF::class.java)?.convert()
                    if (cheese != null)
                        onSuccess(cheese)
                }
            }

            onFailure(it.exception)
        }
    }

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

    private fun dataCompleteListener(
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) = OnCompleteListener<DocumentReference> {
        if (it.isSuccessful)
            onSuccess()
        else
            onFailure(it.exception)
    }
}