package com.malec.cheesetime.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malec.cheesetime.R
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
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
            viewModelScope.launch {
                try {
                    repo.register(email, pass)
                    startMainScreen()
                } catch (e: Exception) {
                    setError(e)
                }
            }
    }

    fun login(email: String?, pass: String?) {
        if (email.isNullOrBlank() || pass.isNullOrBlank())
            loginError.value = context.getString(R.string.empty_fields_error)
        else
            viewModelScope.launch {
                try {
                    repo.login(email, pass)
                    startMainScreen()
                } catch (e: Exception) {
                    setError(e)
                }
            }
    }

    fun googleLogin() {
        router.navigateTo(Screens.GoogleLoginScreen)
    }

    fun handleActivityResult(requestCode: Int, data: Intent?) {
        if (requestCode == Screens.GoogleLoginScreen.requestCode) {
            viewModelScope.launch {
                try {
                    repo.googleLogin(data)
                    startMainScreen()
                } catch (e: Exception) {
                    setError(e)
                }
            }
        }
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