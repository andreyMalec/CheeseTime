package com.malec.signin.presentation.store

import com.malec.store.Reducer

class SignInReducer : Reducer<SignInState, SignInAction> {
    override fun reduce(state: SignInState, action: SignInAction): SignInState {
        return if (action is SignInAction.Login ||
            action is SignInAction.Register ||
            action is SignInAction.GoogleLogin
        ) {
            state.copy(
                isLoading = true,
                error = null
            )
        } else {
            when (action) {
                is SignInAction.Error -> state.copy(
                    isLoading = false,
                    error = action.t
                )
                is SignInAction.SignedIn -> state.copy(
                    isLoading = false,
                    error = null
                )
                is SignInAction.Back -> state.copy(
                    isLoading = false
                )
                is SignInAction.SetEmail -> state.copy(
                    email = action.email
                )
                is SignInAction.SetPassword -> state.copy(
                    password = action.password
                )
                else -> state.copy()
            }
        }
    }
}