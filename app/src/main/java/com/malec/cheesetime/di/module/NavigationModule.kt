package com.malec.cheesetime.di.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.malec.cheeselist.CheeseListScreen
import com.malec.cheesetime.R
import com.malec.presentation.navigation.ScreenContainers
import com.malec.reportlist.ReportListScreen
import com.malec.tasklist.TaskListScreen
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

    @Provides
    @Singleton
    fun screenContainers() = ScreenContainers(
        mapOf(
            Pair(TaskListScreen.screenKey, R.id.tabsFragmentContainer),
            Pair(CheeseListScreen.screenKey, R.id.tabsFragmentContainer),
            Pair(ReportListScreen.screenKey, R.id.tabsFragmentContainer)
        )
    )
}