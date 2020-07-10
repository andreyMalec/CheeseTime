package com.malec.cheesetime.di.module

import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [ContextModule::class])
class DbModule {
//    @Provides
//    @Singleton
//    fun instance(context: Context): CheeseDatabase = CheeseDatabase.instance(context)
//
//    @Provides
//    @Singleton
//    fun dao(db: CheeseDatabase): CheeseDao = db.cheeseDataDao()
}