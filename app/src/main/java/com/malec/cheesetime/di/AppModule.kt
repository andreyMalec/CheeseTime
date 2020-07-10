package com.malec.cheesetime.di

import com.malec.cheesetime.di.activity.ActivityModule
import com.malec.cheesetime.di.viewModule.ViewModelModule
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [ActivityModule::class, ViewModelModule::class, Navigation::class])
class AppModule {
//    @Provides
//    @Singleton
//    fun cheeseRepo(api: CheeseApi, dao: CheeseDao): CheeseRepo = CheeseRepo(api, dao)
}