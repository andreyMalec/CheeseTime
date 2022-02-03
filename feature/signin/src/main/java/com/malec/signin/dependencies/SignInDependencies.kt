package com.malec.signin.dependencies

import com.malec.domain.repository.UserRepo
import com.malec.presentation.unidirectional.BaseDependencies

interface SignInDependencies : BaseDependencies {
    fun getUserRepo(): UserRepo
    fun signInOutput(): SignInOutput
}