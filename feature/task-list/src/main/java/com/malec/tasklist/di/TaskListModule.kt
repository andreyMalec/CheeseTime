package com.malec.tasklist.di

import com.malec.domain.repository.TaskRepo
import com.malec.domain.usecase.DeleteTaskUseCase
import com.malec.store.ErrorHandler
import com.malec.tasklist.dependencies.TaskListOutput
import com.malec.tasklist.presentation.store.TaskListReducer
import com.malec.tasklist.presentation.store.TaskListState
import com.malec.tasklist.presentation.store.TaskListStore
import com.malec.tasklist.presentation.store.actionhandler.OpenDetailActionHandler
import com.malec.tasklist.presentation.store.sideeffect.DeleteSideEffect
import com.malec.tasklist.presentation.store.sideeffect.UpdateTasksSideEffect
import com.malec.tasklist.presentation.store.sideeffect.UpdateTimeSideEffect
import com.malec.tasklist.presentation.viewcontroller.TaskListViewController
import com.malec.tasklist.usecase.GetTasksUseCase
import dagger.Module
import dagger.Provides
import java.util.concurrent.CopyOnWriteArrayList

@Module
class TaskListModule {
    @TaskListScope
    @Provides
    fun provideStore(
        updateTasksSideEffect: UpdateTasksSideEffect,
        updateTimeSideEffect: UpdateTimeSideEffect,
        openDetailActionHandler: OpenDetailActionHandler,
        deleteSideEffect: DeleteSideEffect,
        errorHandler: ErrorHandler
    ): TaskListStore {
        return TaskListStore(
            TaskListState(),
            TaskListReducer(),
            errorHandler,
            sideEffects = CopyOnWriteArrayList(
                listOf(
                    updateTasksSideEffect,
                    updateTimeSideEffect,
                    deleteSideEffect
                )
            ),
            bindActionSources = CopyOnWriteArrayList(
                listOf(
                )
            ),
            actionHandlers = CopyOnWriteArrayList(
                listOf(
                    openDetailActionHandler
                )
            ),
            actionSources = CopyOnWriteArrayList(
                listOf(
                )
            )
        )
    }

    @TaskListScope
    @Provides
    fun provideUpdateTasksSideEffect(useCase: GetTasksUseCase): UpdateTasksSideEffect =
        UpdateTasksSideEffect(useCase)

    @TaskListScope
    @Provides
    fun provideDeleteSideEffect(useCase: DeleteTaskUseCase): DeleteSideEffect =
        DeleteSideEffect(useCase)

    @TaskListScope
    @Provides
    fun provideUpdateTimeSideEffect(): UpdateTimeSideEffect = UpdateTimeSideEffect()

    @TaskListScope
    @Provides
    fun provideGetTasksUseCase(taskRepo: TaskRepo): GetTasksUseCase =
        GetTasksUseCase(taskRepo)

    @TaskListScope
    @Provides
    fun provideOpenDetailActionHandler(taskListOutput: TaskListOutput): OpenDetailActionHandler =
        OpenDetailActionHandler(taskListOutput)

    @TaskListScope
    @Provides
    fun provideViewController(store: TaskListStore): TaskListViewController =
        TaskListViewController(store)
}