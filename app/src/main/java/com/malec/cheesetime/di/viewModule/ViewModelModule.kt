package com.malec.cheesetime.di.viewModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malec.cheesetime.di.module.NetworkModule
import com.malec.cheesetime.di.module.UserStorageModule
import com.malec.cheesetime.ui.MainViewModel
import com.malec.cheesetime.ui.cheeseManage.CheeseManageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module(includes = [UserStorageModule::class, NetworkModule::class])
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheeseManageViewModel::class)
    abstract fun cheeseViewModel(viewModel: CheeseManageViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}