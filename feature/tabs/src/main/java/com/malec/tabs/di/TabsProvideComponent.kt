package com.malec.tabs.di

import com.malec.injection.InjectionHolder
import com.malec.tabs.dependencies.TabsDependencies

object TabsProvideComponent {
    fun provide(componentDependenciesClass: Class<out TabsDependencies>) {
        TabsComponentProvider.injectionFunction = {
            component = DaggerTabsComponent.builder()
                .tabsDependencies(
                    InjectionHolder.instance.findComponent(
                        componentDependenciesClass
                    )
                )
                .tabsModule(TabsModule())
                .build()
        }
    }
}