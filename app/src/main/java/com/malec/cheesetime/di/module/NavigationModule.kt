package com.malec.cheesetime.di.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {
    @Provides
    @Singleton
    fun cicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun router(cicerone: Cicerone<Router>) = cicerone.router

    @Provides
    @Singleton
    fun navHolder(cicerone: Cicerone<Router>) = cicerone.getNavigatorHolder()
}