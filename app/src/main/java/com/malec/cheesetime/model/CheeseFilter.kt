package com.malec.cheesetime.model

import com.malec.cheesetime.util.DateFormatter
import java.util.*

data class CheeseFilter(
    val dateStart: String? = null,
    val dateEnd: String? = null,
    val type: String? = null,
    val archived: Boolean? = null,
    val sortBy: CheeseSort? = null
)

fun List<Cheese>.filteredBy(filter: CheeseFilter): List<Cheese> {
    var filteredList = toList()
    if (filter.dateStart != null) {
        val start = DateFormatter.dateFromString(filter.dateStart)
        filteredList = filteredList.filter { it.date >= start }
    }
    if (filter.dateEnd != null) {
        val end = DateFormatter.dateFromString(filter.dateEnd)
        filteredList = filteredList.filter { it.date <= end }
    }
    if (filter.type != null)
        filteredList = filteredList.filter {
            it.recipe.toLowerCase(Locale.ROOT).contains(filter.type.toLowerCase(Locale.ROOT))
        }
    if (filter.archived != null)
        filteredList = filteredList.filter { it.isArchived == filter.archived }

    filteredList = when (filter.sortBy) {
        CheeseSort.DATE_START -> filteredList.sortedBy { it.date }
        CheeseSort.DATE_END -> filteredList.sortedBy { it.date }
        CheeseSort.TYPE -> filteredList.sortedBy { it.recipe }
        else ->
            filteredList.sortedBy { it.id }
    }
    if (filter.sortBy == null || filter.sortBy == CheeseSort.DATE_END)
        filteredList = filteredList.reversed()

    return filteredList
}