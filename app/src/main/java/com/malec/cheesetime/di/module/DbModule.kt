package com.malec.cheesetime.di.module

import android.content.Context
import com.malec.cheesetime.service.localDb.CheeseDao
import com.malec.cheesetime.service.localDb.CheeseDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ContextModule::class])
class DbModule {
    @Provides
    @Singleton
    fun instance(context: Context): CheeseDatabase = CheeseDatabase.instance(context)

    @Provides
    @Singleton
    fun dao(db: CheeseDatabase): CheeseDao = db.cheeseDataDao()
}