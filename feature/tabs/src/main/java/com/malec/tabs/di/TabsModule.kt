package com.malec.tabs.di

import com.malec.domain.repository.UserRepo
import com.malec.store.ErrorHandler
import com.malec.tabs.dependencies.TabsOutput
import com.malec.tabs.presentation.store.TabsReducer
import com.malec.tabs.presentation.store.TabsState
import com.malec.tabs.presentation.store.TabsStore
import com.malec.tabs.presentation.store.actionhandler.OpenTabActionHandler
import com.malec.tabs.presentation.store.sideeffect.LogoutSideEffect
import com.malec.tabs.presentation.viewcontroller.TabsViewController
import dagger.Module
import dagger.Provides
import java.util.concurrent.CopyOnWriteArrayList

@Module
class TabsModule {
    @TabsScope
    @Provides
    fun provideStore(
        logoutSideEffect: LogoutSideEffect,
        openTabActionHandler: OpenTabActionHandler,
        errorHandler: ErrorHandler
    ): TabsStore {
        return TabsStore(
            TabsState(),
            TabsReducer(),
            errorHandler,
            sideEffects = CopyOnWriteArrayList(
                listOf(
                    logoutSideEffect
                )
            ),
            bindActionSources = CopyOnWriteArrayList(
                listOf(
                )
            ),
            actionHandlers = CopyOnWriteArrayList(
                listOf(
                    openTabActionHandler
                )
            )
        )
    }

    @TabsScope
    @Provides
    fun provideViewController(store: TabsStore): TabsViewController =
        TabsViewController(store)

    @TabsScope
    @Provides
    fun provideLogoutSideEffect(userRepo: UserRepo, tabsOutput: TabsOutput): LogoutSideEffect =
        LogoutSideEffect(userRepo, tabsOutput)

    @TabsScope
    @Provides
    fun provideOpenTabActionHandler(tabsOutput: TabsOutput): OpenTabActionHandler =
        OpenTabActionHandler(tabsOutput)
}