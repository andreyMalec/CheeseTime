package com.malec.domain.repository

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.malec.domain.api.UserApi
import com.malec.domain.model.Recipe
import kotlinx.coroutines.tasks.await

class UserRepo(
    private val api: UserApi,
    private val client: GoogleSignInClient
) {
    fun isUserAuthorized() = api.isUserAuthorized()

    fun getUserLogin() = api.getUserLogin()

    suspend fun logout() {
        api.logout()
        client.signOut().await()
    }

    suspend fun register(email: String, pass: String) = api.register(email, pass)

    suspend fun login(email: String, pass: String) = api.login(email, pass)

    suspend fun getNextRecipeId() = api.getNextRecipeId()

    suspend fun getRecipes() = api.getRecipes()

    suspend fun createRecipe(recipe: Recipe) = api.createRecipe(recipe)

    suspend fun updateRecipe(recipe: Recipe) = api.updateRecipe(recipe)

    suspend fun deleteRecipeById(id: Long) = api.deleteRecipeById(id)

    suspend fun getRecipeById(id: Long) = api.getRecipeById(id)

    suspend fun googleLogin(intent: Intent?) = api.googleLogin(intent)
}