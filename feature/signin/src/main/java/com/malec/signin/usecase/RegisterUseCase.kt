package com.malec.signin.usecase

import com.malec.domain.repository.UserRepo
import com.malec.interactor.UseCase

class RegisterUseCase(
    private val userRepo: UserRepo
) : UseCase<UserUseCaseParams, Unit>() {
    override suspend fun build(params: UserUseCaseParams) {
        userRepo.register(params.email, params.password)
    }
}