package com.malec.cheesetime.model

enum class CheeseSort(val value: String?) {
    DATE_START("Date (start)"),
    DATE_END("Date (end)"),
    TYPE("Cheese type"),
    ID(null);

    companion object {
        fun from(findValue: String?): CheeseSort = values().first { it.value == findValue }
    }
}