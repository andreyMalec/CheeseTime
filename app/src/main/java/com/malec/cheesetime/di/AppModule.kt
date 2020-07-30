package com.malec.cheesetime.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.malec.cheesetime.di.activity.ActivityModule
import com.malec.cheesetime.di.viewModule.ViewModelModule
import com.malec.cheesetime.repo.CheeseRepo
import com.malec.cheesetime.repo.TaskRepo
import com.malec.cheesetime.repo.UserRepo
import com.malec.cheesetime.service.network.CheeseApi
import com.malec.cheesetime.service.network.StorageApi
import com.malec.cheesetime.service.network.TaskApi
import com.malec.cheesetime.util.CheeseSharer
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ActivityModule::class, ViewModelModule::class, Navigation::class])
class AppModule {
    @Provides
    fun cheeseRepo(api: CheeseApi, storageApi: StorageApi, sharer: CheeseSharer): CheeseRepo =
        CheeseRepo(api, storageApi, sharer)

    @Provides
    fun taskRepo(api: TaskApi, cheeseApi: CheeseApi): TaskRepo = TaskRepo(api, cheeseApi)

    @Provides
    fun userRepo(auth: FirebaseAuth, db: FirebaseFirestore, context: Context): UserRepo =
        UserRepo(auth, db, context)

    @Provides
    @Singleton
    fun cheeseSharer(context: Context): CheeseSharer = CheeseSharer(context)
}