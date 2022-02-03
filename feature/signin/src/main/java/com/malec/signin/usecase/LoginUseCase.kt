package com.malec.signin.usecase

import com.malec.domain.repository.UserRepo
import com.malec.interactor.UseCase

class LoginUseCase(
    private val userRepo: UserRepo
) : UseCase<UserUseCaseParams, Unit>() {
    override suspend fun build(params: UserUseCaseParams) {
        userRepo.login(params.email, params.password)
    }
}