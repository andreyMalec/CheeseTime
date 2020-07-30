package com.malec.cheesetime.di.module

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.malec.cheesetime.service.network.CheeseApi
import com.malec.cheesetime.service.network.StorageApi
import com.malec.cheesetime.service.network.TaskApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun database(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun cheeseApi(db: FirebaseFirestore, auth: FirebaseAuth): CheeseApi = CheeseApi(db, auth)

    @Provides
    fun taskApi(db: FirebaseFirestore, auth: FirebaseAuth): TaskApi = TaskApi(db, auth)

    @Provides
    fun auth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun storage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun storageApi(storage: FirebaseStorage, context: Context): StorageApi =
        StorageApi(storage, context)
}