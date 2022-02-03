package com.malec.signin.usecase

import com.malec.domain.repository.UserRepo
import com.malec.interactor.UseCase

class GoogleLoginUseCase(
    private val userRepo: UserRepo
) : UseCase<UserUseCaseParams, Unit>() {
    override suspend fun build(params: UserUseCaseParams) {
        userRepo.googleLogin(params.googleIntent)
    }
}