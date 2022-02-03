package com.malec.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.malec.tabs.presentation.view.TabsFragment

object TabsScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment {
        return TabsFragment.newInstance()
    }
}