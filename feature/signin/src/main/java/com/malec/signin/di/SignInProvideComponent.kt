package com.malec.signin.di

import com.malec.injection.InjectionHolder
import com.malec.signin.dependencies.SignInDependencies

object SignInProvideComponent {
    fun provide(componentDependenciesClass: Class<out SignInDependencies>) {
        SignInComponentProvider.injectionFunction = {
            component = DaggerSignInComponent.builder()
                .signInDependencies(
                    InjectionHolder.instance.findComponent(
                        componentDependenciesClass
                    )
                )
                .signInModule(SignInModule())
                .build()
        }
    }
}