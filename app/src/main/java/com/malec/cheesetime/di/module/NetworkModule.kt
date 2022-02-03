package com.malec.cheesetime.di.module

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.malec.domain.api.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun database(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun auth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun allocator(auth: FirebaseAuth, db: FirebaseFirestore): ReferenceAllocator =
        ReferenceAllocator(auth, db)

    @Provides
    @Singleton
    fun cheeseApi(allocator: ReferenceAllocator): CheeseApi = CheeseApi(allocator)

    @Provides
    @Singleton
    fun taskApi(allocator: ReferenceAllocator): TaskApi = TaskApi(allocator)

    @Provides
    @Singleton
    fun userApi(auth: FirebaseAuth, allocator: ReferenceAllocator): UserApi =
        UserApi(auth, allocator)

    @Provides
    @Singleton
    fun storage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun storageApi(storage: FirebaseStorage, context: Context): StorageApi =
        StorageApi(storage, context.cacheDir)
}