package com.malec.domain.repository

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.malec.domain.R
import com.malec.domain.api.UserApi
import com.malec.domain.model.Recipe
import kotlinx.coroutines.tasks.await

class UserRepo(
    private val api: UserApi,
    private val context: Context
) {
    companion object {
        fun googleSignInClient(context: Context): GoogleSignInClient {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(context, gso)
        }
    }

    fun isUserAuthorized() = api.isUserAuthorized()

    fun getUserLogin() = api.getUserLogin()

    suspend fun logout() {
        api.logout()
        googleSignInClient(context).signOut().await()
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