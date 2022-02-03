package com.malec.cheeselist.usecase

import com.malec.domain.model.CheeseFilter
import com.malec.interactor.UseCaseParams

data class SearchUseCaseParams(
    val query: String = "",
    val filter: CheeseFilter = CheeseFilter.empty,
    val selectedIds: Set<Long> = setOf()
) : UseCaseParams()