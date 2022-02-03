package com.malec.signin.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.malec.domain.data.GoogleLoginResult
import com.malec.injection.ComponentOwner
import com.malec.presentation.base.BaseFragment
import com.malec.presentation.unidirectional.BaseView
import com.malec.signin.R
import com.malec.signin.databinding.FragmentSigninBinding
import com.malec.signin.di.SignInComponent
import com.malec.signin.di.SignInComponentProvider
import com.malec.signin.presentation.store.SignInState
import com.malec.signin.presentation.viewcontroller.SignInViewController
import javax.inject.Inject

class SignInFragment : BaseFragment<FragmentSigninBinding>(), BaseView<SignInState>,
    ComponentOwner<SignInComponent> {
    @Inject
    internal lateinit var viewController: SignInViewController

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentSigninBinding.inflate(inflater, parent, attachToParent)

    override fun renderState(state: SignInState) {
        binding.layoutProgressBar.isVisible = state.isLoading

        state.error?.let {
            showError(it.message ?: getString(R.string.error))
        }

        binding.emailEditText.apply {
            if (text.toString() != state.email)
                setText(state.email)
        }

        binding.passEditText.apply {
            if (text.toString() != state.password)
                setText(state.email)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewController)
    }

    override fun provideComponent() = SignInComponentProvider.getInstance().component

    override fun inject(component: SignInComponent) {
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.loginButton.setOnClickListener {
            viewController.onClickLogin()
        }

        binding.registerButton.setOnClickListener {
            viewController.onClickRegister()
        }

        binding.googleLoginButton.setOnClickListener {
            viewController.onClickGoogleLogin()
        }

        binding.emailEditText.doAfterTextChanged {
            viewController.onEmailChange(it.toString())
        }

        binding.passEditText.setOnEditorActionListener { _, id, _ ->
            return@setOnEditorActionListener if (id == EditorInfo.IME_ACTION_DONE) {
                binding.loginButton.performClick()
                true
            } else false
        }
        binding.passEditText.doAfterTextChanged {
            viewController.onPasswordChange(it.toString())
        }
    }

    override fun onResult(result: Any) {
        super.onResult(result)

        if (result is GoogleLoginResult) {
            viewController.onGoogleLoginResult(result.intent)
        } else
            viewController.onClickExit()
    }

    companion object {
        fun newInstance() = SignInFragment()
    }
}