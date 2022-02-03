package com.malec.cheesedetail.di

import com.malec.cheesedetail.dependencies.CheeseDetailDependencies
import com.malec.cheesedetail.presentation.view.CheeseDetailFragment
import dagger.Component

@CheeseDetailScope
@Component(
    modules = [CheeseDetailModule::class],
    dependencies = [CheeseDetailDependencies::class]
)
interface CheeseDetailComponent {
    fun inject(fragment: CheeseDetailFragment)
}