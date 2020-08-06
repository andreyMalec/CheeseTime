package com.malec.cheesetime.di

import android.content.Context
import com.malec.cheesetime.di.activity.ActivityModule
import com.malec.cheesetime.di.viewModule.ViewModelModule
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.TaskRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.network.CheeseApi
import com.malec.cheesetime.service.network.StorageApi
import com.malec.cheesetime.service.network.TaskApi
import com.malec.cheesetime.service.network.UserApi
import com.malec.cheesetime.util.CheeseSharer
import com.malec.cheesetime.util.PhotoDownloader
import com.malec.cheesetime.util.PhotoSharer
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ActivityModule::class, ViewModelModule::class, Navigation::class])
class AppModule {
    @Provides
    @Singleton
    fun cheeseRepo(api: CheeseApi, storageApi: StorageApi, sharer: CheeseSharer): CheeseRepo =
        CheeseRepo(api, storageApi, sharer)

    @Provides
    @Singleton
    fun taskRepo(api: TaskApi, cheeseApi: CheeseApi): TaskRepo = TaskRepo(api, cheeseApi)

    @Provides
    @Singleton
    fun userRepo(api: UserApi, context: Context): UserRepo = UserRepo(api, context)

    @Provides
    @Singleton
    fun cheeseSharer(context: Context): CheeseSharer = CheeseSharer(context)

    @Provides
    @Singleton
    fun photoDownloader(context: Context): PhotoDownloader = PhotoDownloader(context)

    @Provides
    @Singleton
    fun photoSharer(context: Context): PhotoSharer = PhotoSharer(context)
}