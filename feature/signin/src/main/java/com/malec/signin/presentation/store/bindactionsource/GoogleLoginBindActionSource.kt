package com.malec.signin.presentation.store.bindactionsource

import com.malec.signin.presentation.store.SignInAction
import com.malec.signin.presentation.store.SignInState
import com.malec.signin.usecase.GoogleLoginUseCase
import com.malec.signin.usecase.UserUseCaseParams
import com.malec.store.BindActionSource
import kotlinx.coroutines.flow.flowOf

class GoogleLoginBindActionSource(
    useCase: GoogleLoginUseCase
) : BindActionSource<SignInState, SignInAction>(
    requirement = { action -> action is SignInAction.GoogleLoginResult },
    source = { _, action ->
        action as SignInAction.GoogleLoginResult

        useCase.build(UserUseCaseParams(googleIntent = action.intent))
        flowOf(SignInAction.SignedIn)
    },
    error = { SignInAction.Error(it) }
)