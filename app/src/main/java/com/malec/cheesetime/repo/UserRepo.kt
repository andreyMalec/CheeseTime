package com.malec.cheesetime.repo

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Recipe
import com.malec.cheesetime.service.network.UserApi
import kotlinx.coroutines.tasks.await

class UserRepo(
    private val api: UserApi,
    private val context: Context
) {
    companion object {
        private var _client: GoogleSignInClient? = null
        private val client: GoogleSignInClient
            get() = _client!!

        fun googleSignInClient(context: Context): GoogleSignInClient {
            if (_client != null)
                return client

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            _client = GoogleSignIn.getClient(context, gso)
            return client
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