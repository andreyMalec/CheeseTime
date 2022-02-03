package com.malec.cheesedetail.usecase

import com.malec.interactor.UseCaseParams

data class GetCheeseByIdUseCaseParams(
    val cheeseId: Long
) : UseCaseParams()
