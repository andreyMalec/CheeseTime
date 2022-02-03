package com.malec.main.di

import com.malec.main.dependencies.MainDependencies
import com.malec.main.presentation.view.MainActivity
import dagger.Component

@MainScope
@Component(
    modules = [MainModule::class],
    dependencies = [MainDependencies::class]
)
interface MainComponent {
    fun inject(activity: MainActivity)
}