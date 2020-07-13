package com.malec.cheesetime.di.viewModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malec.cheesetime.di.module.NetworkModule
import com.malec.cheesetime.di.module.UserStorageModule
import com.malec.cheesetime.ui.cheeseManage.CheeseManageViewModel
import com.malec.cheesetime.ui.login.LoginViewModel
import com.malec.cheesetime.ui.main.MainViewModel
import com.malec.cheesetime.ui.main.cheeseList.CheeseListViewModel
import com.malec.cheesetime.ui.main.report.ReportViewModel
import com.malec.cheesetime.ui.main.taskList.TaskListViewModel
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
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

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
    @ViewModelKey(TaskListViewModel::class)
    abstract fun taskListViewModel(viewModel: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel::class)
    abstract fun reportViewModel(viewModel: ReportViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}