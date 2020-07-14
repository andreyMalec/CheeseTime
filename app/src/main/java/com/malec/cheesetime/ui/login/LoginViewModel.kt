package com.malec.cheesetime.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malec.cheesetime.R
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.ui.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val context: Context,
    private val repo: UserRepo,
    private val router: Router
) : ViewModel() {

    val loginError = MutableLiveData<String>(null)

    init {
        if (repo.isUserAuthorized())
            startMainScreen()
    }

    fun register(email: String?, pass: String?) {
        if (email.isNullOrBlank() || pass.isNullOrBlank())
            loginError.value = context.getString(R.string.empty_fields_error)
        else
            repo.register(email, pass, { startMainScreen() }, { setError(it) })
    }

    fun login(email: String?, pass: String?) {
        if (email.isNullOrBlank() || pass.isNullOrBlank())
            loginError.value = context.getString(R.string.empty_fields_error)
        else
            repo.login(email, pass, { startMainScreen() }, { setError(it) })
    }

    private fun startMainScreen() {
        router.newRootScreen(Screens.MainScreen)
    }

    private fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        loginError.value = msg.drop(i + 2)
    }
}