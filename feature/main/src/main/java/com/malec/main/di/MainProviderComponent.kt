package com.malec.main.di

import com.malec.injection.InjectionHolder
import com.malec.main.dependencies.MainDependencies

object MainProviderComponent {
    fun provide(componentDependenciesClass: Class<out MainDependencies>) {
        MainComponentProvider.injectionFunction = {
            val appComponent = DaggerMainComponent.builder()
                .mainDependencies(
                    InjectionHolder.instance.findComponent(
                        componentDependenciesClass
                    )
                )
                .build()

            this.component = appComponent
        }
    }
}