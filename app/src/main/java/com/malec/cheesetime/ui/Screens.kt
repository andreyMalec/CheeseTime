package com.malec.cheesetime.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.malec.cheesetime.ui.login.LoginActivity
import com.malec.cheesetime.ui.main.MainActivity
import com.malec.cheesetime.ui.main.cheeseList.CheeseListFragment
import com.malec.cheesetime.ui.main.report.ReportFragment
import com.malec.cheesetime.ui.main.taskList.TaskListFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

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

    object ReportScreen : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return ReportFragment()
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
}