package com.malec.cheesetime.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malec.cheesetime.R
import com.malec.cheesetime.repo.UserRepo
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val context: Context,
    private val repo: UserRepo
) : ViewModel() {

    val isLoginError = MutableLiveData<String>(null)

    private val _isLoginSuccess = MutableLiveData(false)
    val isLoginSuccess: LiveData<Boolean>
        get() = _isLoginSuccess

    init {
        _isLoginSuccess.value = repo.isUserAuthorized()
    }

    fun register(email: String?, pass: String?) {
        if (email.isNullOrBlank() || pass.isNullOrBlank())
            isLoginError.value = context.getString(R.string.empty_fields_error)
        else
            repo.register(email, pass, { _isLoginSuccess.value = true }, { setError(it) })
    }

    fun login(email: String?, pass: String?) {
        if (email.isNullOrBlank() || pass.isNullOrBlank())
            isLoginError.value = context.getString(R.string.empty_fields_error)
        else
            repo.login(email, pass, { _isLoginSuccess.value = true }, { setError(it) })
    }

    private fun setError(t: Throwable?) {
        val msg = t?.toString() ?: ""
        val i = msg.indexOf(": ")
        isLoginError.value = msg.drop(i + 2)
    }
}