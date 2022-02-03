package com.malec.signin.presentation.store

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: Throwable? = null
)