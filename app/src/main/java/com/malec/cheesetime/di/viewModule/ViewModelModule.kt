package com.malec.cheesetime.di.viewModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malec.cheesetime.di.module.ContextModule
import com.malec.cheesetime.di.module.NetworkModule
import com.malec.cheesetime.ui.main.cheese.cheeseList.CheeseListViewModel
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageViewModel
import com.malec.cheesetime.ui.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [NetworkModule::class, ContextModule::class])
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CheeseManageViewModel::class)
    abstract fun cheeseViewModel(viewModel: CheeseManageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheeseListViewModel::class)
    abstract fun cheeseListViewModel(viewModel: CheeseListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun settingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}