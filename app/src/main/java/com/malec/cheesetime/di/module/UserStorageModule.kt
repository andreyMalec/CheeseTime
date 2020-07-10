package com.malec.cheesetime.di.module

import android.content.Context
import com.malec.cheesetime.localStorage.userStorage.UserSharedPref
import com.malec.cheesetime.localStorage.userStorage.UserStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class UserStorageModule {
    @Provides
    @Singleton
    fun userStorage(context: Context): UserStorage = UserSharedPref(context)
}