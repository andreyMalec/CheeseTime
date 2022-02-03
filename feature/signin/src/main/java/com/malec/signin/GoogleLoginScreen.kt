package com.malec.signin

import android.content.Context
import android.content.Intent
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class GoogleLoginScreen(
    private val client: GoogleSignInClient
): ActivityScreen {
    override val screenKey = GOOGLE_LOGIN
    override fun createIntent(context: Context): Intent {
        return client.signInIntent
    }

    companion object {
        const val GOOGLE_LOGIN = "GoogleLogin"
    }
}