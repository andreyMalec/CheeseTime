package com.malec.cheesetime.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseF
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
        cheese: Cheese,
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        db.collection("cheese").add(cheese).addOnCompleteListener {
            if (it.isSuccessful)
                onSuccess()
            else
                onFailure(it.exception)
        }
    }

    fun update(
        cheese: Cheese,
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        db.collection("cheese").whereEqualTo("id", cheese.id).get().addOnCompleteListener { get ->
            if (get.isSuccessful) {
                val docId = get.result?.documents?.get(0)?.id
                db.collection("cheese").document(docId.toString()).update(cheese.toMap())
                    .addOnCompleteListener { update ->
                        if (update.isSuccessful)
                            onSuccess()
                        else
                            onFailure(update.exception)
                    }
            } else
                onFailure(get.exception)
        }
    }

    fun deleteById(id: Long) {
        db.collection("cheese").whereEqualTo("id", id).get().addOnCompleteListener {
            val docId = it.result?.documents?.get(0)?.id
            db.collection("cheese").document(docId.toString()).delete()
        }
    }
}