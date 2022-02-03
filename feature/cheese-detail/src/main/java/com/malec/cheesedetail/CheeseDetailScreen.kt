package com.malec.cheesedetail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.malec.cheesedetail.presentation.view.CheeseDetailFragment

class CheeseDetailScreen(private val cheeseId: Long?) : FragmentScreen {
    override val screenKey = "CheeseDetail"

    override fun createFragment(factory: FragmentFactory): Fragment {
        return CheeseDetailFragment.newInstance(cheeseId)
    }
}