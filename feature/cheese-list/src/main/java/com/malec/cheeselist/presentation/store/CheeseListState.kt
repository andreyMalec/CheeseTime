package com.malec.cheeselist.presentation.store

import com.malec.domain.model.Cheese
import com.malec.domain.model.CheeseFilter

data class CheeseListState(
    val cheeses: List<Cheese> = listOf(),
    val cheeseTypes: List<String> = listOf(),
    val filter: CheeseFilter = CheeseFilter.empty,
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedIds: Set<Long> = setOf(),
    val selectedCount: Int = 0,
    val error: Throwable? = null
)