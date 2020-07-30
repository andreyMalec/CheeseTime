package com.malec.cheesetime.repo

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

    suspend fun googleLogin(intent: Intent?) {
        val account = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val user = auth.signInWithCredential(credential).await()

        val init = db.collection("${user.user?.uid}cheese").document("init").get().await()
        if (!init.exists())
            createNewCollection(user.user?.uid)
    }

    private fun createNewCollection(userId: String?) {
        db.collection("$userId|task").document("init").set(mapOf("init" to null))
        db.collection("$userId|ids").document("nextCheese").set(mapOf("id" to 1))
        db.collection("$userId|ids").document("nextTask").set(mapOf("id" to 1))
        db.collection("$userId|cheese").document("init").set(mapOf("init" to null))
    }
}