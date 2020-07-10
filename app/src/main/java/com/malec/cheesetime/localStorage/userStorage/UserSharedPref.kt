package com.malec.cheesetime.localStorage.userStorage

import android.content.Context
import com.malec.cheesetime.localStorage.clear
import com.malec.cheesetime.localStorage.get
import com.malec.cheesetime.localStorage.set
import com.malec.cheesetime.model.User

class UserSharedPref(context: Context) : UserStorage {
    private val localStorage =
        context.getSharedPreferences(UserPreferences.PREFERENCES.key, Context.MODE_PRIVATE)

    override fun getUser(): User = User(
        getKey(), getEmail(), getPass()
    )

    override fun isUserAuthorized(): Boolean = localStorage.contains(UserPreferences.KEY.key)

    override fun getKey(): String = localStorage[UserPreferences.KEY.key]

    override fun setKey(value: String) {
        localStorage[UserPreferences.KEY.key] = value
    }

    override fun getEmail(): String = localStorage[UserPreferences.EMAIL.key]

    override fun setEmail(value: String) {
        localStorage[UserPreferences.EMAIL.key] = value
    }

    override fun getPass(): String = localStorage[UserPreferences.PASSWORD.key]

    override fun setPass(value: String) {
        localStorage[UserPreferences.PASSWORD.key] = value
    }

    override fun clear() {
        localStorage.clear()
    }
}