package com.malec.cheesetime.di

import com.malec.cheesedetail.dependencies.CheeseDetailDependencies
import com.malec.cheeselist.dependencies.CheeseListDependencies
import com.malec.cheesetime.app.App
import com.malec.main.dependencies.MainDependencies
import com.malec.reportlist.dependencies.ReportListDependencies
import com.malec.signin.dependencies.SignInDependencies
import com.malec.tabs.dependencies.TabsDependencies
import com.malec.taskdetail.dependencies.TaskDetailDependencies
import com.malec.tasklist.dependencies.TaskListDependencies
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface AppComponent : MainDependencies,
    SignInDependencies,
    TabsDependencies,
    TaskListDependencies,
    CheeseListDependencies,
    ReportListDependencies,
    TaskDetailDependencies,
    CheeseDetailDependencies {
    fun inject(app: App)
}