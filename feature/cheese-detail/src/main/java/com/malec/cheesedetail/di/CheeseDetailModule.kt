package com.malec.cheesedetail.di

import com.malec.cheesedetail.dependencies.CheeseDetailOutput
import com.malec.cheesedetail.presentation.store.CheeseDetailReducer
import com.malec.cheesedetail.presentation.store.CheeseDetailState
import com.malec.cheesedetail.presentation.store.CheeseDetailStore
import com.malec.cheesedetail.presentation.store.actionhandler.BackActionHandler
import com.malec.cheesedetail.presentation.store.sideeffect.CheckCanSaveSideEffect
import com.malec.cheesedetail.presentation.store.sideeffect.LoadCheeseSideEffect
import com.malec.cheesedetail.presentation.store.sideeffect.ShowCheeseSideEffect
import com.malec.cheesedetail.presentation.viewcontroller.CheeseDetailViewController
import com.malec.cheesedetail.usecase.GetCheeseByIdUseCase
import com.malec.domain.repository.CheeseRepo
import com.malec.domain.usecase.GetRecipesUseCase
import com.malec.store.ErrorHandler
import dagger.Module
import dagger.Provides
import java.util.concurrent.CopyOnWriteArrayList

@Module
class CheeseDetailModule {
    @CheeseDetailScope
    @Provides
    fun provideStore(
        backActionHandler: BackActionHandler,
        loadCheeseSideEffect: LoadCheeseSideEffect,
        showCheeseSideEffect: ShowCheeseSideEffect,
        checkCanSaveSideEffect: CheckCanSaveSideEffect,
        errorHandler: ErrorHandler
    ): CheeseDetailStore {
        return CheeseDetailStore(
            CheeseDetailState(),
            CheeseDetailReducer(),
            errorHandler,
            sideEffects = CopyOnWriteArrayList(
                listOf(
                    loadCheeseSideEffect,
                    showCheeseSideEffect,
                    checkCanSaveSideEffect
                )
            ),
            bindActionSources = CopyOnWriteArrayList(
                listOf(
                )
            ),
            actionHandlers = CopyOnWriteArrayList(
                listOf(
                    backActionHandler
                )
            ),
            actionSources = CopyOnWriteArrayList(
                listOf(
                )
            )
        )
    }

    @CheeseDetailScope
    @Provides
    fun provideBackActionHandler(output: CheeseDetailOutput): BackActionHandler =
        BackActionHandler(output)

    @CheeseDetailScope
    @Provides
    fun provideLoadCheeseSideEffect(useCase: GetCheeseByIdUseCase): LoadCheeseSideEffect =
        LoadCheeseSideEffect(useCase)

    @CheeseDetailScope
    @Provides
    fun provideCheckCanSaveSideEffect(): CheckCanSaveSideEffect =
        CheckCanSaveSideEffect()

    @CheeseDetailScope
    @Provides
    fun provideShowCheeseSideEffect(useCase: GetRecipesUseCase): ShowCheeseSideEffect =
        ShowCheeseSideEffect(useCase)

    @CheeseDetailScope
    @Provides
    fun provideGetCheeseByIdUseCase(cheeseRepo: CheeseRepo): GetCheeseByIdUseCase =
        GetCheeseByIdUseCase(cheeseRepo)

    @CheeseDetailScope
    @Provides
    fun provideViewController(store: CheeseDetailStore): CheeseDetailViewController =
        CheeseDetailViewController(store)
}