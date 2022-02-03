package com.malec.taskdetail.usecase

import com.malec.domain.util.DateFormatter
import com.malec.interactor.UseCase

class GetTimeAtPositionUseCase : UseCase<GetTimeAtPositionUseCaseParams, Long>() {
    override suspend fun build(params: GetTimeAtPositionUseCaseParams): Long {
        return when (params.position) {
            0 -> DateFormatter.millisecondsInMinute * 10
            1 -> DateFormatter.millisecondsInMinute * 15
            2 -> DateFormatter.millisecondsIn30Min
            3 -> DateFormatter.millisecondsInHour
            4 -> DateFormatter.millisecondsInHour * 3
            5 -> DateFormatter.millisecondsInHour * 5
            6 -> DateFormatter.millisecondsInDay
            else -> 0L
        }
    }
}