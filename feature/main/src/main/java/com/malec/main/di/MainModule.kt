package com.malec.main.di

import com.malec.domain.repository.UserRepo
import com.malec.main.dependencies.MainOutput
import com.malec.main.presentation.store.InitActionHandler
import com.malec.main.presentation.store.MainReducer
import com.malec.main.presentation.store.MainState
import com.malec.main.presentation.store.MainStore
import com.malec.main.presentation.viewcontroller.MainViewController
import com.malec.store.ErrorHandler
import dagger.Module
import dagger.Provides
import java.util.concurrent.CopyOnWriteArrayList

@Module
class MainModule {
    @MainScope
    @Provides
    fun provideStore(
        initActionHandler: InitActionHandler,
        errorHandler: ErrorHandler
    ) = MainStore(
        MainState(),
        MainReducer(),
        errorHandler,
        actionHandlers = CopyOnWriteArrayList(
            listOf(
                initActionHandler
            )
        )
    )

    @MainScope
    @Provides
    fun provideInitActionHandler(userRepo: UserRepo, mainOutput: MainOutput): InitActionHandler =
        InitActionHandler(userRepo, mainOutput)

    @MainScope
    @Provides
    fun provideViewController(store: MainStore) = MainViewController(store)
}