package com.malec.cheesetime.di.module

import com.github.terrakok.cicerone.Router
import com.malec.cheesedetail.dependencies.CheeseDetailOutput
import com.malec.cheeselist.dependencies.CheeseListOutput
import com.malec.cheesetime.coordinator.*
import com.malec.main.dependencies.MainOutput
import com.malec.signin.dependencies.SignInOutput
import com.malec.tabs.dependencies.TabsOutput
import com.malec.taskdetail.dependencies.TaskDetailOutput
import com.malec.tasklist.dependencies.TaskListOutput
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoordinatorModule {

    @Provides
    @Singleton
    fun mainOutput(router: Router): MainOutput =
        MainCoordinator(router)

    @Provides
    @Singleton
    fun signInOutput(router: Router): SignInOutput =
        SignInCoordinator(router)

    @Provides
    @Singleton
    fun tabsOutput(router: Router): TabsOutput =
        TabsCoordinator(router)

    @Provides
    @Singleton
    fun cheeseListOutput(router: Router): CheeseListOutput =
        CheeseListCoordinator(router)

    @Provides
    @Singleton
    fun taskListOutput(router: Router): TaskListOutput =
        TaskListCoordinator(router)

    @Provides
    @Singleton
    fun taskDetailOutput(router: Router): TaskDetailOutput =
        TaskDetailCoordinator(router)

    @Provides
    @Singleton
    fun cheeseDetailOutput(router: Router): CheeseDetailOutput =
        CheeseDetailCoordinator(router)
}