package com.malec.tasklist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.malec.tasklist.presentation.view.TaskListFragment

object TaskListScreen : FragmentScreen {
    override val screenKey = "TaskList"

    override fun createFragment(factory: FragmentFactory): Fragment {
        return TaskListFragment.newInstance()
    }
}