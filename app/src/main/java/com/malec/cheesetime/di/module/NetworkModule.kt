package com.malec.cheesetime.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.malec.cheesetime.service.network.CheeseApi
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
    fun api(db: FirebaseFirestore): CheeseApi = CheeseApi(db)
}