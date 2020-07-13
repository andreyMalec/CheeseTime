package com.malec.cheesetime.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.malec.cheesetime.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navHolder: NavigatorHolder

    private val navigator = SupportAppNavigator(this, -1)

    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initClickListeners()
        initViewModelListeners()
    }

    private fun initViewModelListeners() {
        viewModel.isLoginError.observe(this, Observer { message ->
            if (message != null) {
                showError(message)
                viewModel.isLoginError.value = null
            }
        })
    }

    private fun showError(message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorError))
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .show()
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