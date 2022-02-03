package com.malec.reportlist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.malec.reportlist.presentation.ReportListFragment

object ReportListScreen : FragmentScreen {
    override val screenKey = "ReportList"

    override fun createFragment(factory: FragmentFactory): Fragment {
        return ReportListFragment.newInstance()
    }
}