package com.malec.signin.usecase

import android.content.Intent
import com.malec.interactor.UseCaseParams

data class UserUseCaseParams(
    val email: String = "",
    val password: String = "",
    val googleIntent: Intent? = null
) : UseCaseParams()