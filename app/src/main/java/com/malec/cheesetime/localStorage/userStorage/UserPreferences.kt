package com.malec.cheesetime.localStorage.userStorage

enum class UserPreferences(val key: String) {
    PREFERENCES("UserData"),

    EMAIL("Email"), PASSWORD("Password"), KEY("UserKey")
}