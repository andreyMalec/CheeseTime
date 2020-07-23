package com.malec.cheesetime.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepo(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {
    fun isUserAuthorized() = auth.currentUser != null

    suspend fun register(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).await().also {
            createNewCollection(it.user?.uid)
        }
    }

    suspend fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
    }

    private fun createNewCollection(userId: String?) {
        db.collection("$userId|task").document("init").set(mapOf("init" to null))
        db.collection("$userId|ids").document("nextCheese").set(mapOf("id" to 1))
        db.collection("$userId|ids").document("nextTask").set(mapOf("id" to 1))
        db.collection("$userId|cheese").document("init").set(mapOf("init" to null))
    }
}