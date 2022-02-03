package com.malec.cheeselist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.malec.cheeselist.presentation.view.CheeseListFragment

object CheeseListScreen : FragmentScreen {
    override val screenKey = "CheeseList"

    override fun createFragment(factory: FragmentFactory): Fragment {
        return CheeseListFragment.newInstance()
    }
}