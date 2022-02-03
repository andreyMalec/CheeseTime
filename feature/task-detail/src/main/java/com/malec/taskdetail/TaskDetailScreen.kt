package com.malec.taskdetail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.malec.taskdetail.presentation.view.TaskDetailFragment

data class TaskDetailScreen(private val taskId: Long?) : FragmentScreen {
    override val screenKey = "TaskDetail"

    override fun createFragment(factory: FragmentFactory): Fragment {
        return TaskDetailFragment.newInstance(taskId)
    }
}