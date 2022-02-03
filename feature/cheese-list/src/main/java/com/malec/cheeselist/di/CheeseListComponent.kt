package com.malec.cheeselist.di

import com.malec.cheeselist.dependencies.CheeseListDependencies
import com.malec.cheeselist.presentation.view.CheeseListFragment
import dagger.Component

@CheeseListScope
@Component(
    modules = [CheeseListModule::class],
    dependencies = [CheeseListDependencies::class]
)
interface CheeseListComponent {
    fun inject(fragment: CheeseListFragment)
}