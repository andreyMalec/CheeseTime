package com.malec.cheesetime.di.activity

import com.malec.cheesetime.ui.main.cheese.cheeseList.CheeseListFragment
import com.malec.cheesetime.ui.main.report.ReportsFragment
import com.malec.cheesetime.ui.main.task.taskList.TaskListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeTaskListFragment(): TaskListFragment

    @ContributesAndroidInjector
    abstract fun contributeCheeseListFragment(): CheeseListFragment

    @ContributesAndroidInjector
    abstract fun contributeReportsFragment(): ReportsFragment
}