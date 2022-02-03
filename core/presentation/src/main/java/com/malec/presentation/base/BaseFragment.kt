package com.malec.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.malec.presentation.R

abstract class BaseFragment<Binding : ViewBinding> : Fragment() {
    private var _binding: Binding? = null
    protected val binding get() = _binding!!

    abstract fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createViewBinding(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun onResult(result: Any) {
        childFragmentManager.fragments.forEach {
            if (it is BaseFragment<*>)
                onResult(result)
        }
    }

    protected fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorError))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            .show()
    }

    protected open fun hideSoftKeyboard() {
        if (activity?.currentFocus != null) {
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } else {
            activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            )
        }
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }
}