package com.malec.domain.api

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.toObject
import com.malec.domain.model.Recipe
import com.malec.domain.model.RecipeF
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

    suspend fun getRecipes(): List<Recipe> {
        val data = ref.recipes().get().await()
        val recipes = data.documents.map { it.toObject<RecipeF>()?.convert() }
        return recipes.filterNotNull()
    }

    suspend fun getRecipeById(id: Long): Recipe? {
        val docs = getQueryById(id).documents
        return docs[0].toObject<RecipeF>()?.convert()
    }

    private suspend fun getQueryById(id: Long) =
        ref.recipes().whereEqualTo("id", id).get().await()

    suspend fun createRecipe(recipe: Recipe) {
        ref.recipes().add(recipe.toMap()).await()
    }

    suspend fun updateRecipe(recipe: Recipe) {
        try {
            val docId = getQueryById(recipe.id).documents[0]?.id
            ref.recipes().document(docId.toString()).update(recipe.toMap()).await()
        } catch (e: Exception) {
            createRecipe(recipe)
        }
    }

    suspend fun deleteRecipeById(id: Long) {
        val docId = getQueryById(id).documents[0]?.id
        ref.recipes().document(docId.toString()).delete()
    }

    suspend fun getNextRecipeId(): Long {
        val data = ref.nextRecipe().get().await()
        val id = data.getLong(ReferenceAllocator.ID)
        return id?.also {
            ref.nextRecipe().update(ReferenceAllocator.ID, id + 1)
        } ?: 0
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
        ref.cheesesInit().set(mapOf(ReferenceAllocator.INIT to null))
        ref.tasksInit().set(mapOf(ReferenceAllocator.INIT to null))
        ref.recipesInit().set(mapOf(ReferenceAllocator.INIT to null))
        ref.nextCheese().set(mapOf(ReferenceAllocator.ID to 1))
        ref.nextTask().set(mapOf(ReferenceAllocator.ID to 1))
        ref.nextRecipe().set(mapOf(ReferenceAllocator.ID to 1))
    }
}