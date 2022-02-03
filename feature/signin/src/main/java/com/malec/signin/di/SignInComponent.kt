package com.malec.signin.di

import com.malec.signin.dependencies.SignInDependencies
import com.malec.signin.presentation.view.SignInFragment
import dagger.Component

@SignInScope
@Component(
    modules = [SignInModule::class],
    dependencies = [SignInDependencies::class]
)
interface SignInComponent {
    fun inject(signInFragment: SignInFragment)
}