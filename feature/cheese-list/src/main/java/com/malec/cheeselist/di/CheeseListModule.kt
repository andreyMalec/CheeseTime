package com.malec.cheeselist.di

import com.malec.cheeselist.dependencies.CheeseListOutput
import com.malec.cheeselist.presentation.store.CheeseListReducer
import com.malec.cheeselist.presentation.store.CheeseListState
import com.malec.cheeselist.presentation.store.CheeseListStore
import com.malec.cheeselist.presentation.store.actionsource.UpdateCheeseTypesActionSource
import com.malec.cheeselist.presentation.store.sideeffect.*
import com.malec.cheeselist.presentation.viewcontroller.CheeseListViewController
import com.malec.cheeselist.usecase.UpdateCheesesUseCase
import com.malec.domain.repository.CheeseRepo
import com.malec.domain.usecase.DeleteCheeseUseCase
import com.malec.domain.usecase.GetRecipesUseCase
import com.malec.domain.usecase.ShareCheesesUseCase
import com.malec.domain.usecase.UpdateCheeseUseCase
import com.malec.presentation.Resources
import com.malec.store.ErrorHandler
import dagger.Module
import dagger.Provides
import java.util.concurrent.CopyOnWriteArrayList

@Module
class CheeseListModule {
    @CheeseListScope
    @Provides
    fun provideStore(
        updateCheesesSideEffect: UpdateCheesesSideEffect,
        searchSideEffect: SearchSideEffect,
        filterSideEffect: FilterSideEffect,
        toggleSelectSideEffect: ToggleSelectSideEffect,
        openDetailSideEffect: OpenDetailSideEffect,
        unselectAllSideEffect: UnselectAllSideEffect,
        archiveSelectedSideEffect: ArchiveSelectedSideEffect,
        printSelectedSideEffect: PrintSelectedSideEffect,
        deleteSelectedSideEffect: DeleteSelectedSideEffect,
        deleteSideEffect: DeleteSideEffect,
        updateCheeseTypesActionSource: UpdateCheeseTypesActionSource,
        errorHandler: ErrorHandler
    ): CheeseListStore {
        return CheeseListStore(
            CheeseListState(),
            CheeseListReducer(),
            errorHandler,
            sideEffects = CopyOnWriteArrayList(
                listOf(
                    updateCheesesSideEffect,
                    searchSideEffect,
                    filterSideEffect,
                    toggleSelectSideEffect,
                    openDetailSideEffect,
                    unselectAllSideEffect,
                    archiveSelectedSideEffect,
                    printSelectedSideEffect,
                    deleteSelectedSideEffect,
                    deleteSideEffect
                )
            ),
            bindActionSources = CopyOnWriteArrayList(
                listOf(
                )
            ),
            actionHandlers = CopyOnWriteArrayList(
                listOf(

                )
            ),
            actionSources = CopyOnWriteArrayList(
                listOf(
                    updateCheeseTypesActionSource
                )
            )
        )
    }

    @CheeseListScope
    @Provides
    fun provideViewController(store: CheeseListStore): CheeseListViewController =
        CheeseListViewController(store)

    @CheeseListScope
    @Provides
    fun provideUpdateCheeseSideEffect(useCase: UpdateCheesesUseCase): UpdateCheesesSideEffect =
        UpdateCheesesSideEffect(useCase)

    @CheeseListScope
    @Provides
    fun provideUpdateCheesesUseCase(cheeseRepo: CheeseRepo): UpdateCheesesUseCase =
        UpdateCheesesUseCase(cheeseRepo)

    @CheeseListScope
    @Provides
    fun provideSearchSideEffect(useCase: UpdateCheesesUseCase): SearchSideEffect =
        SearchSideEffect(useCase)

    @CheeseListScope
    @Provides
    fun provideFilterSideEffect(useCase: UpdateCheesesUseCase): FilterSideEffect =
        FilterSideEffect(useCase)

    @CheeseListScope
    @Provides
    fun provideToggleSelectSideEffect(): ToggleSelectSideEffect = ToggleSelectSideEffect()

    @CheeseListScope
    @Provides
    fun provideUnselectAllSideEffect(): UnselectAllSideEffect = UnselectAllSideEffect()

    @CheeseListScope
    @Provides
    fun provideArchiveSelectedSideEffect(useCase: UpdateCheeseUseCase): ArchiveSelectedSideEffect =
        ArchiveSelectedSideEffect(useCase)

    @CheeseListScope
    @Provides
    fun providePrintSelectedSideEffect(useCase: ShareCheesesUseCase): PrintSelectedSideEffect =
        PrintSelectedSideEffect(useCase)

    @CheeseListScope
    @Provides
    fun provideDeleteSelectedSideEffect(useCase: DeleteCheeseUseCase): DeleteSelectedSideEffect =
        DeleteSelectedSideEffect(useCase)

    @CheeseListScope
    @Provides
    fun provideDeleteSideEffect(useCase: DeleteCheeseUseCase): DeleteSideEffect =
        DeleteSideEffect(useCase)

    @CheeseListScope
    @Provides
    fun provideOpenDetailSideEffect(cheeseListOutput: CheeseListOutput): OpenDetailSideEffect =
        OpenDetailSideEffect(cheeseListOutput)

    @CheeseListScope
    @Provides
    fun provideUpdateCheeseTypesActionSource(
        useCase: GetRecipesUseCase,
        resources: Resources
    ): UpdateCheeseTypesActionSource =
        UpdateCheeseTypesActionSource(useCase, resources)
}