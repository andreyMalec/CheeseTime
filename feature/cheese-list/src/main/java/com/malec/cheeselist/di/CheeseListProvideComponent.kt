package com.malec.cheeselist.di

import com.malec.cheeselist.dependencies.CheeseListDependencies
import com.malec.injection.InjectionHolder

object CheeseListProvideComponent {
    fun provide(componentDependenciesClass: Class<out CheeseListDependencies>) {
        CheeseListComponentProvider.injectionFunction = {
            component = DaggerCheeseListComponent.builder()
                .cheeseListDependencies(
                    InjectionHolder.instance.findComponent(
                        componentDependenciesClass
                    )
                )
                .cheeseListModule(CheeseListModule())
                .build()
        }
    }
}