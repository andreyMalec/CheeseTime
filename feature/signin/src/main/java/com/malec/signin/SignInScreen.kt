package com.malec.signin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.malec.signin.presentation.view.SignInFragment

object SignInScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment {
        return SignInFragment.newInstance()
    }
}