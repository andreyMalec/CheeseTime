package com.malec.cheesetime.repo

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.malec.cheesetime.R
import com.malec.cheesetime.service.network.UserApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class UserRepo(
    private val api: UserApi,
    private val context: Context
) {
    companion object {
        fun googleSignInClient(context: Context): GoogleSignInClient? {
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
        googleSignInClient(context)?.signOut()?.await()
    }

    suspend fun register(email: String, pass: String) = api.register(email, pass)

    suspend fun login(email: String, pass: String) = api.login(email, pass)

    suspend fun getRecipes(): List<String> {
        val data = api.getRecipes()
        return data.split("♂")
    }

    suspend fun googleLogin(intent: Intent?) = api.googleLogin(intent)
}