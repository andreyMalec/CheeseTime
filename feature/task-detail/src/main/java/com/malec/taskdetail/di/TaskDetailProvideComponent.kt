package com.malec.taskdetail.di

import com.malec.injection.InjectionHolder
import com.malec.taskdetail.dependencies.TaskDetailDependencies

object TaskDetailProvideComponent {
    fun provide(componentDependenciesClass: Class<out TaskDetailDependencies>) {
        TaskDetailComponentProvider.injectionFunction = {
            component = DaggerTaskDetailComponent.builder()
                .taskDetailDependencies(
                    InjectionHolder.instance.findComponent(
                        componentDependenciesClass
                    )
                )
                .taskDetailModule(TaskDetailModule())
                .build()
        }
    }
}