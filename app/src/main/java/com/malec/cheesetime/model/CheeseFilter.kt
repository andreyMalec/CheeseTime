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
        filteredList = filteredList.sortedBy {
            when (filter.sortBy) {
                CheeseSort.DATE_START -> it.date.toString()
                CheeseSort.DATE_END -> it.date.toString()
                CheeseSort.TYPE -> it.recipe
                else -> it.id.toString()
            }
        }
        if (filter.sortBy == CheeseSort.DATE_END)
            filteredList = filteredList.reversed()
    }
    return filteredList
}