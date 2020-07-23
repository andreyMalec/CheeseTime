package com.malec.cheesetime.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.malec.cheesetime.R
import com.malec.cheesetime.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginActivity : BaseActivity() {
    @Inject
    lateinit var navHolder: NavigatorHolder

    private val navigator = SupportAppNavigator(this, -1)

    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initClickListeners()
        initViewModelListeners()
    }

    private fun initViewModelListeners() {
        viewModel.loginError.observe(this, Observer { message ->
            if (message != null) {
                showError(message)
                viewModel.loginError.value = null
            }
        })
    }

    private fun initClickListeners() {
        loginButton.setOnClickListener {
            val email = emailEditText.text?.toString()?.trim()
            val pass = passEditText.text?.toString()?.trim()
            viewModel.login(email, pass)
        }

        registerButton.setOnClickListener {
            val email = emailEditText.text?.toString()?.trim()
            val pass = passEditText.text?.toString()?.trim()
            viewModel.register(email, pass)
        }
    }

    override fun onPause() {
        super.onPause()
        navHolder.removeNavigator()
    }

    override fun onResume() {
        super.onResume()
        navHolder.setNavigator(navigator)
    }
}