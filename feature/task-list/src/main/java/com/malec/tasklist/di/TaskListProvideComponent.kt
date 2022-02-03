package com.malec.tasklist.di

import com.malec.injection.InjectionHolder
import com.malec.tasklist.dependencies.TaskListDependencies

object TaskListProvideComponent {
    fun provide(componentDependenciesClass: Class<out TaskListDependencies>) {
        TaskListComponentProvider.injectionFunction = {
            component = DaggerTaskListComponent.builder()
                .taskListDependencies(
                    InjectionHolder.instance.findComponent(
                        componentDependenciesClass
                    )
                )
                .taskListModule(TaskListModule())
                .build()
        }
    }
}