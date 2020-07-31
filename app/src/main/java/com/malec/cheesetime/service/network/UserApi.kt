package com.malec.cheesetime.service.network

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class UserApi(
    private val auth: FirebaseAuth,
    private val ref: ReferenceAllocator
) {
    fun isUserAuthorized() = auth.currentUser != null

    fun getUserLogin() = auth.currentUser?.email

    fun logout() {
        auth.signOut()
    }

    suspend fun register(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).await().also {
            ref.updateUser(it.user)
            createNewCollection()
        }
    }

    suspend fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await().also {
            ref.updateUser(it.user)
        }
    }

    suspend fun getRecipes(): String {
        val data = ref.recipes().get().await()
        return data.getString("list") ?: ""
    }

    suspend fun setRecipes(recipes: String) {
        ref.recipes().update("list", recipes).await()
    }

    suspend fun googleLogin(intent: Intent?) {
        val account = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).await().also {
            ref.updateUser(it.user)
        }

        val init = ref.cheesesInit().get().await()
        if (!init.exists())
            createNewCollection()
    }

    private fun createNewCollection() {
        ref.cheesesInit().set(mapOf("init" to null))
        ref.tasksInit().set(mapOf("init" to null))
        ref.recipes().set(mapOf("list" to ""))
        ref.nextCheese().set(mapOf("id" to 1))
        ref.nextTask().set(mapOf("id" to 1))
    }
}