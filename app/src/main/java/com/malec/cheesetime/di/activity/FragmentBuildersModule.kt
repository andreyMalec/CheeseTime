package com.malec.cheesetime.di.activity

import com.malec.cheesetime.ui.main.cheeseList.CheeseListFragment
import com.malec.cheesetime.ui.main.report.ReportsFragment
import com.malec.cheesetime.ui.main.taskList.TaskListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeTaskListFragment(): TaskListFragment

    @ContributesAndroidInjector
    abstract fun contributeCheeseListFragment(): CheeseListFragment

    @ContributesAndroidInjector
    abstract fun contributeReportsFragment(): ReportsFragment
}