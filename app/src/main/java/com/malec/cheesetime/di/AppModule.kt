package com.malec.cheesetime.di

import android.content.Context
import com.malec.cheesetime.di.activity.ActivityModule
import com.malec.cheesetime.di.viewModule.ViewModelModule
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.network.CheeseApi
import com.malec.cheesetime.util.CheeseSharer
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ActivityModule::class, ViewModelModule::class, Navigation::class])
class AppModule {
    @Provides
    @Singleton
    fun cheeseRepo(api: CheeseApi, sharer: CheeseSharer): CheeseRepo = CheeseRepo(api, sharer)

    @Provides
    @Singleton
    fun userRepo(): UserRepo = UserRepo()

    @Provides
    @Singleton
    fun cheeseSharer(context: Context): CheeseSharer = CheeseSharer(context)
}