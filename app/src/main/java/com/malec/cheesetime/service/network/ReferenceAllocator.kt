package com.malec.cheesetime.service.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings

class ReferenceAllocator(
    auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    private var userId = auth.currentUser?.uid

    fun updateUser(user: FirebaseUser?) {
        user?.let {
            userId = it.uid
        }
    }

    fun tasks() = db.collection("$userId").document("data").collection("tasks")

    fun tasksInit() = db.collection("$userId").document("data").collection("tasks").document("init")

    fun cheeses() = db.collection("$userId").document("data").collection("cheeses")

    fun cheesesInit() =
        db.collection("$userId").document("data").collection("cheeses").document("init")

    fun recipes() =
        db.collection("$userId").document("profile").collection("prefs").document("recipes")

    fun nextCheese() =
        db.collection("$userId").document("data").collection("ids").document("nextCheese")

    fun nextTask() =
        db.collection("$userId").document("data").collection("ids").document("nextTask")
}