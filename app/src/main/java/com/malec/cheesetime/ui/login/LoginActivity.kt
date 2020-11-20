package com.malec.cheesetime.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivityLoginBinding
import com.malec.cheesetime.ui.base.BaseActivity
import com.malec.cheesetime.ui.main.ResultNavigator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    override val navigator = ResultNavigator(
        this,
        supportFragmentManager,
        R.id.navHostFragment
    )

    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.user = viewModel.user.value

        initViewModelListeners()
        initClickListeners()
    }

    private fun initViewModelListeners() {
        viewModel.loginError.observe(this, { message ->
            if (message != null) {
                showError(message)
                viewModel.loginError.value = null
            }
        })
    }

    private fun initClickListeners() {
        passEditText.setOnEditorActionListener { _, id, _ ->
            return@setOnEditorActionListener if (id == EditorInfo.IME_ACTION_DONE) {
                loginButton.performClick()
                true
            } else false
        }

        loginButton.setOnClickListener {
            viewModel.login()
        }

        registerButton.setOnClickListener {
            viewModel.register()
        }

        googleLoginButton.setOnClickListener {
            viewModel.googleLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            viewModel.handleActivityResult(requestCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }
}