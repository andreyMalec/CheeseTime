package com.malec.cheesetime.di.activity

import com.malec.cheesetime.di.module.DbModule
import com.malec.cheesetime.di.module.NetworkModule
import com.malec.cheesetime.ui.MainActivity
import com.malec.cheesetime.ui.cheeseManage.CheeseManageActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [NetworkModule::class, DbModule::class])
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

//    @ContributesAndroidInjector
//    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeCheeseManageActivity(): CheeseManageActivity
}