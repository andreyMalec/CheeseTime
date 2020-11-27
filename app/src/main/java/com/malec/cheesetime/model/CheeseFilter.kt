package com.malec.cheesetime.model

import com.malec.cheesetime.util.DateFormatter
import java.util.*

data class CheeseFilter(
    val dateStart: String? = null,
    val dateEnd: String? = null,
    val type: String? = null,
    val archived: Boolean? = false,
    val sortBy: CheeseSort? = null
)

fun List<Cheese>.filteredBy(filter: CheeseFilter): List<Cheese> {
    var filteredList = toList()
    filter.dateStart?.let { dateStart ->
        val start = DateFormatter.dateFromString(dateStart)
        filteredList = filteredList.filter { it.date >= start }
    }
    filter.dateEnd?.let { dateEnd ->
        val end = DateFormatter.dateFromString(dateEnd)
        filteredList = filteredList.filter { it.date <= end }
    }
    filter.type?.let { type ->
        filteredList = filteredList.filter {
            it.recipe.toLowerCase(Locale.ROOT).contains(type.toLowerCase(Locale.ROOT))
        }
    }
    filter.archived?.let { archived ->
        filteredList = filteredList.filter { it.isArchived == archived }
    }

    filteredList = when (filter.sortBy) {
        CheeseSort.DATE_START -> filteredList.sortedBy { it.date }
        CheeseSort.DATE_END -> filteredList.sortedBy { it.date }.reversed()
        CheeseSort.TYPE -> filteredList.sortedBy { it.recipe }
        else -> filteredList.sortedBy { it.id }.reversed()
    }

    return filteredList
}