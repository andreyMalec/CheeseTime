package com.malec.signin.presentation.store.sideeffect

import com.malec.signin.presentation.store.SignInAction
import com.malec.signin.presentation.store.SignInState
import com.malec.signin.usecase.RegisterUseCase
import com.malec.signin.usecase.UserUseCaseParams
import com.malec.store.SideEffect

class RegisterSideEffect(
    private val useCase: RegisterUseCase
) : SideEffect<SignInState, SignInAction>(
    requirement = { _, action -> action is SignInAction.Register },
    effect = { state, _ ->
        useCase.build(UserUseCaseParams(state.email, state.password))
        SignInAction.SignedIn
    },
    error = { SignInAction.Error(it) }
)