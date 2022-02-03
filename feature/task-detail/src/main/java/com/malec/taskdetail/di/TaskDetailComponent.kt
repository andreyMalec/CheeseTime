package com.malec.taskdetail.di

import com.malec.taskdetail.dependencies.TaskDetailDependencies
import com.malec.taskdetail.presentation.view.TaskDetailFragment
import dagger.Component

@TaskDetailScope
@Component(
    modules = [TaskDetailModule::class],
    dependencies = [TaskDetailDependencies::class]
)
interface TaskDetailComponent {
    fun inject(fragment: TaskDetailFragment)
}