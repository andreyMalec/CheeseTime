package com.malec.signin.presentation.store.actionhandler

import com.malec.signin.dependencies.SignInOutput
import com.malec.signin.presentation.store.SignInAction
import com.malec.signin.presentation.store.SignInState
import com.malec.store.ActionHandler

class SignedInActionHandler(
    private val signInOutput: SignInOutput
) : ActionHandler<SignInState, SignInAction>(
    requirement = { action -> action is SignInAction.SignedIn },
    handler = { _, _ ->
        signInOutput.nextScreen()
    }
)