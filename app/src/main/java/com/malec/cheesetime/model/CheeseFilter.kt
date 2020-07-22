package com.malec.cheesetime.model

import com.malec.cheesetime.util.CheeseCreator

data class CheeseFilter(
    val dateStart: String? = null,
    val dateEnd: String? = null,
    val type: String? = null,
    val sortBy: CheeseSort? = null
)

fun List<Cheese>.filteredBy(filter: CheeseFilter): List<Cheese> {
    var filteredList = toList()
    if (filter.dateStart != null) {
        val start = CheeseCreator.dateFromString(filter.dateStart)
        filteredList = filteredList.filter { it.date >= start }
    }
    if (filter.dateEnd != null) {
        val end = CheeseCreator.dateFromString(filter.dateEnd)
        filteredList = filteredList.filter { it.date <= end }
    }
    if (filter.type != null)
        filteredList = filteredList.filter {
            it.recipe.toLowerCase().contains(filter.type.toLowerCase())
        }
    if (filter.sortBy != null) {
        filteredList = when (filter.sortBy) {
            CheeseSort.DATE_START -> filteredList.sortedBy { it.date }
            CheeseSort.DATE_END -> filteredList.sortedBy { it.date }
            CheeseSort.TYPE -> filteredList.sortedBy { it.recipe }
            else -> filteredList.sortedBy { it.id }
        }
        if (filter.sortBy == CheeseSort.DATE_END)
            filteredList = filteredList.reversed()
    }
    return filteredList
}