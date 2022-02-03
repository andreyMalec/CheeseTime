package com.malec.tabs.di

import com.malec.tabs.dependencies.TabsDependencies
import com.malec.tabs.presentation.view.TabsFragment
import dagger.Component

@TabsScope
@Component(
    modules = [TabsModule::class],
    dependencies = [TabsDependencies::class]
)
interface TabsComponent {
    fun inject(tabsFragment: TabsFragment)
}