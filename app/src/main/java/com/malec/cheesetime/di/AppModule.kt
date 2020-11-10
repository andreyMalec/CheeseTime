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
import com.malec.cheesetime.service.notifications.TaskScheduler
import com.malec.cheesetime.util.CheeseSharer
import com.malec.cheesetime.util.PhotoDownloader
import com.malec.cheesetime.util.PhotoSharer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ActivityModule::class, ViewModelModule::class, Navigation::class])
class AppModule {
    @Provides
    @Singleton
    fun cheeseRepo(api: CheeseApi, storageApi: StorageApi, sharer: CheeseSharer): CheeseRepo =
        CheeseRepo(api, storageApi, sharer)

    @Provides
    @Singleton
    fun taskRepo(api: TaskApi, cheeseApi: CheeseApi, scheduler: TaskScheduler): TaskRepo =
        TaskRepo(api, cheeseApi, scheduler)

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

    @Provides
    @Singleton
    fun taskScheduler(context: Context): TaskScheduler = TaskScheduler(context)
}