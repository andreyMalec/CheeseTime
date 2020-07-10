package com.malec.cheesetime.di.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun database(): DatabaseReference = FirebaseDatabase.getInstance().reference

//    @Provides
//    @Singleton
//    fun api(db: DatabaseReference): CheeseApi = FirebaseCheeseApi(db)
}