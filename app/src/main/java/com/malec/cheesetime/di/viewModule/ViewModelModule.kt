package com.malec.cheesetime.di.viewModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malec.cheesetime.di.module.ContextModule
import com.malec.cheesetime.di.module.NetworkModule
import com.malec.cheesetime.ui.login.LoginViewModel
import com.malec.cheesetime.ui.main.MainViewModel
import com.malec.cheesetime.ui.main.cheese.cheeseList.CheeseListViewModel
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageViewModel
import com.malec.cheesetime.ui.main.cheese.cheeseManage.fullscreenPhoto.FullscreenPhotoViewModel
import com.malec.cheesetime.ui.main.report.ReportsViewModel
import com.malec.cheesetime.ui.main.task.taskList.TaskListViewModel
import com.malec.cheesetime.ui.main.task.taskManage.TaskManageViewModel
import com.malec.cheesetime.ui.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [NetworkModule::class, ContextModule::class])
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheeseManageViewModel::class)
    abstract fun cheeseViewModel(viewModel: CheeseManageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullscreenPhotoViewModel::class)
    abstract fun photoViewModel(viewModel: FullscreenPhotoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheeseListViewModel::class)
    abstract fun cheeseListViewModel(viewModel: CheeseListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskListViewModel::class)
    abstract fun taskListViewModel(viewModel: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskManageViewModel::class)
    abstract fun taskViewModel(viewModel: TaskManageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportsViewModel::class)
    abstract fun reportsViewModel(viewModel: ReportsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun settingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}