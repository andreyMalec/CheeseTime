package com.malec.taskdetail.usecase

import com.malec.interactor.UseCaseParams

data class GetTimeAtPositionUseCaseParams(
    val position: Int
) : UseCaseParams()