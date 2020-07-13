package com.malec.cheesetime.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.malec.cheesetime.ui.cheeseManage.CheeseManageActivity
import com.malec.cheesetime.ui.login.LoginActivity
import com.malec.cheesetime.ui.main.MainActivity
import com.malec.cheesetime.ui.main.cheeseList.CheeseListFragment
import com.malec.cheesetime.ui.main.report.ReportsFragment
import com.malec.cheesetime.ui.main.taskList.TaskListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.android.support.SupportAppScreen

@ExperimentalCoroutinesApi
object Screens {
    object CheeseListScreen : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return CheeseListFragment()
        }
    }

    object TaskListScreen : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return TaskListFragment()
        }
    }

    object ReportsScreen : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return ReportsFragment()
        }
    }

    object MainScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, MainActivity::class.java)
        }
    }

    object LoginScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, LoginActivity::class.java)
        }
    }

    object CheeseManageScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, CheeseManageActivity::class.java)
        }
    }
}