package com.malec.cheesedetail.di

import com.malec.cheesedetail.dependencies.CheeseDetailDependencies
import com.malec.injection.InjectionHolder

object CheeseDetailProvideComponent {
    fun provide(componentDependenciesClass: Class<out CheeseDetailDependencies>) {
        CheeseDetailComponentProvider.injectionFunction = {
            component = DaggerCheeseDetailComponent.builder()
                .cheeseDetailDependencies(
                    InjectionHolder.instance.findComponent(
                        componentDependenciesClass
                    )
                )
                .cheeseDetailModule(CheeseDetailModule())
                .build()
        }
    }
}