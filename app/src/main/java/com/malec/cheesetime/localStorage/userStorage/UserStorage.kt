package com.malec.cheesetime.localStorage.userStorage

import com.malec.cheesetime.model.User

interface UserStorage {
    fun getUser(): User

    fun isUserAuthorized(): Boolean

    fun getKey(): String
    fun setKey(value: String)

    fun getEmail(): String
    fun setEmail(value: String)

    fun getPass(): String
    fun setPass(value: String)

    fun clear()
}