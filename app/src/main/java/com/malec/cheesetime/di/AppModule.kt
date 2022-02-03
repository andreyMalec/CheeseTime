package com.malec.cheesetime.di

import android.content.Context
import android.util.Log
import com.malec.cheesetime.di.module.CoordinatorModule
import com.malec.cheesetime.di.module.NavigationModule
import com.malec.cheesetime.di.module.UseCaseModule
import com.malec.cheesetime.di.viewModule.ViewModelModule
import com.malec.cheesetime.resultContract.GoogleLoginResultContract
import com.malec.cheesetime.resultContract.PhotoResultContract
import com.malec.cheesetime.resultContract.ScanResultContract
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.util.BitmapDecoder
import com.malec.cheesetime.util.PhotoDownloader
import com.malec.cheesetime.util.PhotoSharer
import com.malec.domain.api.CheeseApi
import com.malec.domain.api.StorageApi
import com.malec.domain.api.TaskApi
import com.malec.domain.api.UserApi
import com.malec.domain.repository.CheeseRepo
import com.malec.domain.repository.TaskRepo
import com.malec.domain.repository.TaskScheduler
import com.malec.domain.util.CheeseSharer
import com.malec.main.dependencies.MainActivityResultContracts
import com.malec.presentation.Resources
import com.malec.store.ErrorHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelModule::class,
        NavigationModule::class,
        UseCaseModule::class,
        CoordinatorModule::class
    ]
)
class AppModule {
    @Provides
    @Singleton
    fun cheeseRepo(api: CheeseApi, storageApi: StorageApi, sharer: CheeseSharer): CheeseRepo =
        CheeseRepo(api, storageApi, sharer)

    @Provides
    @Singleton
    fun taskRepo(api: TaskApi, scheduler: TaskScheduler): TaskRepo =
        TaskRepo(api, scheduler)

    @Provides
    @Singleton
    fun userRepo(api: UserApi, context: Context): com.malec.domain.repository.UserRepo =
        com.malec.domain.repository.UserRepo(api, context)

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

    @Provides
    @Singleton
    fun resources(context: Context): Resources = Resources(context)

    @Provides
    @Singleton
    fun bitmapDecoder(context: Context): BitmapDecoder = BitmapDecoder(context)

    @Provides
    @Singleton
    fun errorHandler(): ErrorHandler = object : ErrorHandler {
        override fun handle(t: Throwable) {
            Log.e("testa", "", t)
        }
    }

    @Provides
    @Singleton
    fun mainActivityResultContracts(bitmapDecoder: BitmapDecoder) = MainActivityResultContracts(
        mapOf(
            Pair(
                Screens.ScanScreen.screenKey,
                ScanResultContract(Screens.ScanScreen)
            ),
            Pair(
                Screens.CameraPickScreen.screenKey,
                PhotoResultContract(Screens.CameraPickScreen, bitmapDecoder)
            ),
            Pair(
                Screens.GalleryPickScreen.screenKey,
                PhotoResultContract(Screens.GalleryPickScreen, bitmapDecoder)
            ),
            Pair(
                Screens.GoogleLoginScreen.screenKey,
                GoogleLoginResultContract(Screens.GoogleLoginScreen)
            )
        )
    )
}