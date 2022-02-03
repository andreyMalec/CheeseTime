package com.malec.signin.presentation.store.actionhandler

import com.malec.signin.dependencies.SignInOutput
import com.malec.signin.presentation.store.SignInAction
import com.malec.store.ActionHandler

class GoogleLoginActionHandler(
    private val signInOutput: SignInOutput
) : ActionHandler<SignInAction>(
    requirement = { action -> action is SignInAction.GoogleLogin },
    handler = { _ ->
        signInOutput.onGoogleLogin()
    }
)