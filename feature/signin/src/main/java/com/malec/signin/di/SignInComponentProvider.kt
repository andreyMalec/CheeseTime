package com.malec.signin.di

import javax.inject.Inject

class SignInComponentProvider {

    @Inject
    lateinit var component: SignInComponent

    companion object {
        var injectionFunction: (SignInComponentProvider.() -> Unit)? = null

        private val instance = SignInComponentProvider()

        @JvmStatic
        internal fun getInstance(): SignInComponentProvider {
            injectionFunction?.invoke(instance)
            return instance
        }
    }
}