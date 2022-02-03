package com.malec.taskdetail.di

import com.malec.domain.repository.CheeseRepo
import com.malec.domain.repository.TaskRepo
import com.malec.domain.usecase.DeleteTaskUseCase
import com.malec.store.ErrorHandler
import com.malec.taskdetail.dependencies.TaskDetailOutput
import com.malec.taskdetail.presentation.store.TaskDetailReducer
import com.malec.taskdetail.presentation.store.TaskDetailState
import com.malec.taskdetail.presentation.store.TaskDetailStore
import com.malec.taskdetail.presentation.store.actionhandler.BackActionHandler
import com.malec.taskdetail.presentation.store.sideeffect.*
import com.malec.taskdetail.presentation.viewcontroller.TaskDetailViewController
import com.malec.taskdetail.usecase.GetCheesesUseCase
import com.malec.taskdetail.usecase.GetTaskByIdUseCase
import com.malec.taskdetail.usecase.GetTimeAtPositionUseCase
import com.malec.taskdetail.usecase.SaveTaskUseCase
import dagger.Module
import dagger.Provides
import java.util.concurrent.CopyOnWriteArrayList

@Module
class TaskDetailModule {
    @TaskDetailScope
    @Provides
    fun provideStore(
        backActionHandler: BackActionHandler,
        loadTaskSideEffect: LoadTaskSideEffect,
        showTaskSideEffect: ShowTaskSideEffect,
        selectCheeseAtPositionSideEffect: SelectCheeseAtPositionSideEffect,
        checkCanSaveSideEffect: CheckCanSaveSideEffect,
        selectTimeAtPositionSideEffect: SelectTimeAtPositionSideEffect,
        saveTaskSideEffect: SaveTaskSideEffect,
        deleteTaskSideEffect: DeleteTaskSideEffect,
        errorHandler: ErrorHandler
    ): TaskDetailStore {
        return TaskDetailStore(
            TaskDetailState(),
            TaskDetailReducer(),
            errorHandler,
            sideEffects = CopyOnWriteArrayList(
                listOf(
                    showTaskSideEffect,
                    loadTaskSideEffect,
                    selectCheeseAtPositionSideEffect,
                    checkCanSaveSideEffect,
                    selectTimeAtPositionSideEffect,
                    saveTaskSideEffect,
                    deleteTaskSideEffect
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

    @TaskDetailScope
    @Provides
    fun provideBackActionHandler(taskDetailOutput: TaskDetailOutput): BackActionHandler =
        BackActionHandler(taskDetailOutput)

    @TaskDetailScope
    @Provides
    fun provideSelectTimeAtPositionSideEffect(
        useCase: GetTimeAtPositionUseCase
    ): SelectTimeAtPositionSideEffect = SelectTimeAtPositionSideEffect(useCase)

    @TaskDetailScope
    @Provides
    fun provideSaveTaskSideEffect(useCase: SaveTaskUseCase): SaveTaskSideEffect =
        SaveTaskSideEffect(useCase)

    @TaskDetailScope
    @Provides
    fun provideDeleteTaskSideEffect(useCase: DeleteTaskUseCase): DeleteTaskSideEffect =
        DeleteTaskSideEffect(useCase)

    @TaskDetailScope
    @Provides
    fun provideCheckCanSaveSideEffect(): CheckCanSaveSideEffect = CheckCanSaveSideEffect()

    @TaskDetailScope
    @Provides
    fun provideLoadTaskSideEffect(useCase: GetTaskByIdUseCase): LoadTaskSideEffect =
        LoadTaskSideEffect(useCase)

    @TaskDetailScope
    @Provides
    fun provideShowTaskSideEffect(useCase: GetCheesesUseCase): ShowTaskSideEffect =
        ShowTaskSideEffect(useCase)

    @TaskDetailScope
    @Provides
    fun provideSelectCheeseAtPositionSideEffect(): SelectCheeseAtPositionSideEffect =
        SelectCheeseAtPositionSideEffect()

    @TaskDetailScope
    @Provides
    fun provideGetTaskByIdUseCase(taskRepo: TaskRepo): GetTaskByIdUseCase =
        GetTaskByIdUseCase(taskRepo)

    @TaskDetailScope
    @Provides
    fun provideGetCheesesUseCase(cheeseRepo: CheeseRepo): GetCheesesUseCase =
        GetCheesesUseCase(cheeseRepo)

    @TaskDetailScope
    @Provides
    fun provideSaveTaskUseCase(taskRepo: TaskRepo): SaveTaskUseCase =
        SaveTaskUseCase(taskRepo)

    @TaskDetailScope
    @Provides
    fun provideGetTimeAtPositionUseCase(): GetTimeAtPositionUseCase =
        GetTimeAtPositionUseCase()

    @TaskDetailScope
    @Provides
    fun provideViewController(store: TaskDetailStore): TaskDetailViewController =
        TaskDetailViewController(store)
}