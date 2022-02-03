package com.malec.signin.dependencies

interface SignInOutput {
    fun onGoogleLogin()

    fun nextScreen()

    fun exit()
}