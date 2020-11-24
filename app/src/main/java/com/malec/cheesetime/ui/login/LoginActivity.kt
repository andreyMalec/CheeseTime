package com.malec.cheesetime.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivityLoginBinding
import com.malec.cheesetime.ui.base.BaseActivity
import com.malec.cheesetime.ui.main.ResultNavigator

class LoginActivity : BaseActivity() {
    override val navigator = ResultNavigator(
        this,
        R.id.navHostFragment
    )

    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
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
        binding.passEditText.setOnEditorActionListener { _, id, _ ->
            return@setOnEditorActionListener if (id == EditorInfo.IME_ACTION_DONE) {
                binding.loginButton.performClick()
                true
            } else false
        }

        binding.loginButton.setOnClickListener {
            viewModel.login()
        }

        binding.registerButton.setOnClickListener {
            viewModel.register()
        }

        binding.googleLoginButton.setOnClickListener {
            viewModel.googleLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            viewModel.handleActivityResult(requestCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }
}