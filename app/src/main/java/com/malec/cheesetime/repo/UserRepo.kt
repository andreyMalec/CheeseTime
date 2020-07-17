package com.malec.cheesetime.repo

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class UserRepo {
    private val auth = FirebaseAuth.getInstance()

    fun isUserAuthorized() = auth.currentUser != null

    suspend fun register(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).await()
    }

    suspend fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
    }
}