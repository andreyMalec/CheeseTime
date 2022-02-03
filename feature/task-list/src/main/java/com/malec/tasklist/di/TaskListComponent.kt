package com.malec.tasklist.di

import com.malec.tasklist.dependencies.TaskListDependencies
import com.malec.tasklist.presentation.view.TaskListFragment
import dagger.Component

@TaskListScope
@Component(
    modules = [TaskListModule::class],
    dependencies = [TaskListDependencies::class]
)
interface TaskListComponent {
    fun inject(fragment: TaskListFragment)
}