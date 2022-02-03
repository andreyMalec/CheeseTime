package com.malec.signin.presentation.store

import android.content.Intent

sealed class SignInAction {
    object Ignore : SignInAction()
    object Back : SignInAction()
    object SignedIn : SignInAction()

    data class Error(val t: Throwable) : SignInAction()

    data class SetEmail(val email: String) : SignInAction()
    data class SetPassword(val password: String) : SignInAction()

    object Login : SignInAction()
    object Register : SignInAction()
    object GoogleLogin : SignInAction()

    data class GoogleLoginResult(
        val intent: Intent
    ) : SignInAction()
}