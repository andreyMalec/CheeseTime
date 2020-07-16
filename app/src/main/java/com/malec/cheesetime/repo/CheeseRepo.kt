package com.malec.cheesetime.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseF
import com.malec.cheesetime.util.CheeseCreator
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

    fun getAllFiltered(
        onSuccess: (cheeses: List<Cheese>) -> Unit,
        onFailure: (t: Throwable?) -> Unit,
        dateStart: String? = null,
        dateEnd: String? = null,
        cheeseType: String? = null,
        sortBy: String? = null
    ) {
        getAll({ list ->
            var filtered = list
            if (dateStart != null) {
                val start = CheeseCreator.dateFromString(dateStart)
                filtered = filtered.filter { it.date >= start }
            }
            if (dateEnd != null) {
                val end = CheeseCreator.dateFromString(dateEnd)
                filtered = filtered.filter { it.date <= end }
            }
            if (cheeseType != null)
                filtered = filtered.filter {
                    it.recipe.toLowerCase().contains(cheeseType.toLowerCase())
                }
            if (sortBy != null) {
                filtered = filtered.sortedBy {
                    when (sortBy) {
                        "Date (start)" -> it.date.toString()
                        "Date (end)" -> it.date.toString()
                        else -> it.recipe
                    }
                }
                if (sortBy == "Date (end)")
                    filtered = filtered.reversed()
            }

            onSuccess(filtered)
        }, {
            onFailure(it)
        })
    }

//    fun getAllFiltered(
//        onSuccess: (cheeses: List<Cheese>) -> Unit,
//        onFailure: (t: Throwable?) -> Unit,
//        dateStart: String? = null,
//        dateEnd: String? = null,
//        cheeseType: String? = null
//    ) {
//        var query: Query = db.collection("cheese")
//        if (dateStart != null) {
//            val start = CheeseCreator.dateFromString(dateStart)
//            query = query.whereGreaterThan("date", start)
//        }
//        if (dateEnd != null) {
//            val end = CheeseCreator.dateFromString(dateEnd)
//            query = query.whereLessThan("date", end)
//        }
//        if (cheeseType != null)
//            query = query.whereEqualTo("recipe", cheeseType)
//
//        query.get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                val result = it.result?.documents
//                if (result != null) {
//                    val cheeses = result.map { doc ->
//                        doc.toObject(CheeseF::class.java)?.convert()
//                    }
//                    onSuccess(cheeses.filterNotNull())
//
//                    return@addOnCompleteListener
//                }
//            }
//
//            onFailure(it.exception)
//        }
//    }

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