package com.malec.signin.di

import com.malec.domain.repository.UserRepo
import com.malec.signin.dependencies.SignInOutput
import com.malec.signin.presentation.store.SignInReducer
import com.malec.signin.presentation.store.SignInState
import com.malec.signin.presentation.store.SignInStore
import com.malec.signin.presentation.store.actionhandler.GoogleLoginActionHandler
import com.malec.signin.presentation.store.actionhandler.SignedInActionHandler
import com.malec.signin.presentation.store.bindactionsource.GoogleLoginBindActionSource
import com.malec.signin.presentation.store.sideeffect.LoginSideEffect
import com.malec.signin.presentation.store.sideeffect.RegisterSideEffect
import com.malec.signin.presentation.viewcontroller.SignInViewController
import com.malec.signin.usecase.GoogleLoginUseCase
import com.malec.signin.usecase.LoginUseCase
import com.malec.signin.usecase.RegisterUseCase
import com.malec.store.ErrorHandler
import dagger.Module
import dagger.Provides
import java.util.concurrent.CopyOnWriteArrayList

@Module
class SignInModule {
    @SignInScope
    @Provides
    fun provideStore(
        loginSideEffect: LoginSideEffect,
        registerSideEffect: RegisterSideEffect,
        googleLoginBindActionSource: GoogleLoginBindActionSource,
        signedInActionHandler: SignedInActionHandler,
        googleLoginActionHandler: GoogleLoginActionHandler,
        errorHandler: ErrorHandler
    ): SignInStore {
        return SignInStore(
            SignInState(),
            SignInReducer(),
            errorHandler,
            sideEffects = CopyOnWriteArrayList(
                listOf(
                    loginSideEffect,
                    registerSideEffect
                )
            ),
            bindActionSources = CopyOnWriteArrayList(
                listOf(
                    googleLoginBindActionSource
                )
            ),
            actionHandlers = CopyOnWriteArrayList(
                listOf(
                    googleLoginActionHandler,
                    signedInActionHandler
                )
            )
        )
    }

    @SignInScope
    @Provides
    fun provideLoginSideEffect(useCase: LoginUseCase): LoginSideEffect =
        LoginSideEffect(useCase)

    @SignInScope
    @Provides
    fun provideRegisterSideEffect(useCase: RegisterUseCase): RegisterSideEffect =
        RegisterSideEffect(useCase)

    @SignInScope
    @Provides
    fun provideGoogleLoginBindActionSource(useCase: GoogleLoginUseCase): GoogleLoginBindActionSource =
        GoogleLoginBindActionSource(useCase)

    @SignInScope
    @Provides
    fun provideLoginUseCase(userRepo: UserRepo): LoginUseCase =
        LoginUseCase(userRepo)

    @SignInScope
    @Provides
    fun provideRegisterUseCase(userRepo: UserRepo): RegisterUseCase =
        RegisterUseCase(userRepo)

    @SignInScope
    @Provides
    fun provideGoogleLoginUseCase(userRepo: UserRepo): GoogleLoginUseCase =
        GoogleLoginUseCase(userRepo)

    @SignInScope
    @Provides
    fun provideGoogleLoginActionHandler(signInOutput: SignInOutput): GoogleLoginActionHandler =
        GoogleLoginActionHandler(signInOutput)

    @SignInScope
    @Provides
    fun provideSignedInActionHandler(signInOutput: SignInOutput): SignedInActionHandler =
        SignedInActionHandler(signInOutput)

    @SignInScope
    @Provides
    fun provideViewController(store: SignInStore): SignInViewController =
        SignInViewController(store)
}