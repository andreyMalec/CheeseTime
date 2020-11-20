package com.malec.cheesetime.ui.login

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.malec.cheesetime.model.User
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.Resources
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.base.BaseViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repo: UserRepo,
    private val res: Resources,
    private val router: Router
) : BaseViewModel() {

    val loginError = MutableLiveData<String>(null)

    val user = MutableLiveData(User("", ""))

    init {
        if (repo.isUserAuthorized())
            startMainScreen()
    }

    fun register() = enter(user.value?.login, user.value?.pass) { notNullEmail, notNullPass ->
        repo.register(notNullEmail, notNullPass)
    }

    fun login() = enter(user.value?.login, user.value?.pass) { notNullEmail, notNullPass ->
        repo.login(notNullEmail, notNullPass)
    }

    private fun enter(email: String?, pass: String?, enter: suspend (String, String) -> Unit) {
        if (email.isNullOrBlank() || pass.isNullOrBlank())
            loginError.value = res.stringEmptyFieldsError()
        else
            safeRun {
                enter(email.trim(), pass.trim())
                startMainScreen()
            }
    }

    private fun startMainScreen() = router.newRootScreen(Screens.MainScreen)

    fun googleLogin() = router.navigateTo(Screens.GoogleLoginScreen)

    fun handleActivityResult(requestCode: Int, data: Intent?) {
        if (requestCode == Screens.GoogleLoginScreen.requestCode)
            safeRun {
                repo.googleLogin(data)
                startMainScreen()
            }
    }

    override fun setError(t: Throwable?) {
        val msg = t.toString()
        val i = msg.indexOf(": ")
        loginError.value = msg.drop(i + 2)
    }
}