package com.malec.signin.presentation.store.sideeffect

import com.malec.signin.presentation.store.SignInAction
import com.malec.signin.presentation.store.SignInState
import com.malec.signin.usecase.LoginUseCase
import com.malec.signin.usecase.UserUseCaseParams
import com.malec.store.SideEffect

class LoginSideEffect(
    private val useCase: LoginUseCase
) : SideEffect<SignInState, SignInAction>(
    requirement = { _, action -> action is SignInAction.Login },
    effect = { state, _ ->
        useCase.build(UserUseCaseParams(state.email, state.password))
        SignInAction.SignedIn
    },
    error = { SignInAction.Error(it) }
)