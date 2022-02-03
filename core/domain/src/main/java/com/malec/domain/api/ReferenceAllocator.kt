package com.malec.domain.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings

class ReferenceAllocator(
    auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private var userId = auth.currentUser?.uid

    private var root: CollectionReference = rootCollection()

    init {
        db.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }

    fun updateUser(user: FirebaseUser?) {
        user?.let {
            userId = it.uid
        }
        root = rootCollection()
    }

    private fun rootCollection() = db.collection("$userId")

    fun tasks() = root.document(DATA).collection(TASKS)

    fun tasksInit() = root.document(DATA).collection(TASKS).document(INIT)

    fun cheeses() = root.document(DATA).collection(CHEESES)

    fun cheesesInit() =
        root.document(DATA).collection(CHEESES).document(INIT)

    fun recipes() =
        root.document(PREFS).collection(RECIPES)

    fun recipesInit() =
        root.document(PREFS).collection(RECIPES).document(INIT)

    fun nextCheese() =
        root.document(DATA).collection(IDS).document(NEXT_CHEESE)

    fun nextTask() =
        root.document(DATA).collection(IDS).document(NEXT_TASK)

    fun nextRecipe() =
        root.document(DATA).collection(IDS).document(NEXT_RECIPE)

    companion object {
        private const val DATA = "data"
        private const val TASKS = "tasks"
        private const val CHEESES = "cheeses"
        private const val PREFS = "prefs"
        private const val RECIPES = "recipes"
        private const val IDS = "ids"
        private const val NEXT_CHEESE = "nextCheese"
        private const val NEXT_TASK = "nextTask"
        private const val NEXT_RECIPE = "nextRecipe"

        const val INIT = "init"
        const val ID = "id"
    }
}