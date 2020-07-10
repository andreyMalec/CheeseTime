package com.malec.cheesetime.di

import com.malec.cheesetime.app.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface AppComponent {
    fun inject(app: App)
}