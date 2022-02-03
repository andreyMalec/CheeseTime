package com.malec.cheesetime.di.module

import com.malec.domain.repository.CheeseRepo
import com.malec.domain.repository.TaskRepo
import com.malec.domain.repository.UserRepo
import com.malec.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Provides
    @Singleton
    fun provideGetRecipesUseCase(userRepo: UserRepo): GetRecipesUseCase =
        GetRecipesUseCase(userRepo)

    @Provides
    @Singleton
    fun provideUpdateCheeseUseCase(cheeseRepo: CheeseRepo): UpdateCheeseUseCase =
        UpdateCheeseUseCase(cheeseRepo)

    @Provides
    @Singleton
    fun provideDeleteCheeseUseCase(cheeseRepo: CheeseRepo): DeleteCheeseUseCase =
        DeleteCheeseUseCase(cheeseRepo)

    @Provides
    @Singleton
    fun provideShareCheesesUseCase(cheeseRepo: CheeseRepo): ShareCheesesUseCase =
        ShareCheesesUseCase(cheeseRepo)

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(taskRepo: TaskRepo): DeleteTaskUseCase =
        DeleteTaskUseCase(taskRepo)
}