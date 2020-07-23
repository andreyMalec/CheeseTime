package com.malec.cheesetime.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.malec.cheesetime.service.network.CheeseApi
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
    @Singleton
    fun cheeseApi(db: FirebaseFirestore): CheeseApi = CheeseApi(db)

    @Provides
    @Singleton
    fun taskApi(db: FirebaseFirestore): TaskApi = TaskApi(db)

    @Provides
    @Singleton
    fun auth(): FirebaseAuth = FirebaseAuth.getInstance()
}