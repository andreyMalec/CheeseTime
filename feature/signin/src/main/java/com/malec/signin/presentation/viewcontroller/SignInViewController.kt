package com.malec.signin.presentation.viewcontroller

import android.content.Intent
import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.unidirectional.BaseViewController
import com.malec.signin.presentation.store.SignInAction
import com.malec.signin.presentation.store.SignInState
import com.malec.signin.presentation.store.SignInStore

class SignInViewController(
    store: SignInStore
) : BaseViewController<SignInState, SignInAction, BaseView<SignInState>>(store) {
    fun onClickLogin() {
        dispatchAction(SignInAction.Login)
    }

    fun onClickRegister() {
        dispatchAction(SignInAction.Register)
    }

    fun onClickGoogleLogin() {
        dispatchAction(SignInAction.GoogleLogin)
    }

    fun onGoogleLoginResult(intent: Intent) {
        dispatchAction(SignInAction.GoogleLoginResult(intent))
    }

    fun onEmailChange(email: String) {
        dispatchAction(SignInAction.SetEmail(email))
    }

    fun onPasswordChange(password: String) {
        dispatchAction(SignInAction.SetPassword(password))
    }

    fun onClickExit() {
        dispatchAction(SignInAction.Back)
    }
}