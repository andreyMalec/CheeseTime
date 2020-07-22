package com.malec.cheesetime.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.ui.login.LoginActivity
import com.malec.cheesetime.ui.main.MainActivity
import com.malec.cheesetime.ui.main.cheese.cheeseList.CheeseListFragment
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageActivity
import com.malec.cheesetime.ui.main.report.ReportsFragment
import com.malec.cheesetime.ui.main.task.taskList.TaskListFragment
import com.malec.cheesetime.ui.main.task.taskManage.TaskManageActivity
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

    class TaskManageScreen(private var task: Task? = null) : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return task?.let { createExtraIntent(context, it) }
                ?: Intent(context, TaskManageActivity::class.java)
        }

        companion object {
            fun createExtraIntent(context: Context, task: Task) =
                Intent(context, CheeseManageActivity::class.java).apply {
                    putExtra("id", task.id)
                    putExtra("cheeseId", task.cheeseId)
                    putExtra("cheeseName", task.cheeseName)
                    putExtra("todo", task.todo)
                    putExtra("date", task.date)
                    putExtra("comment", task.comment)
                }

            fun parseExtraIntent(intent: Intent) =
                Task(
                    intent.getLongExtra("id", 0),
                    intent.getLongExtra("cheeseId", 0),
                    intent.getStringExtra("cheeseName") ?: "",
                    intent.getStringExtra("todo") ?: "",
                    intent.getLongExtra("date", 0),
                    intent.getStringExtra("comment") ?: ""
                )
        }
    }

    class CheeseManageScreen(private var cheese: Cheese? = null) : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return cheese?.let { createExtraIntent(context, it) }
                ?: Intent(context, CheeseManageActivity::class.java)
        }

        companion object {
            fun createExtraIntent(context: Context, cheese: Cheese) =
                Intent(context, CheeseManageActivity::class.java).apply {
                    putExtra("id", cheese.id)
                    putExtra("name", cheese.name)
                    putExtra("dateStart", cheese.dateStart)
                    putExtra("date", cheese.date)
                    putExtra("recipe", cheese.recipe)
                    putExtra("comment", cheese.comment)
                    putExtra("milk", cheese.milk)
                    putExtra("composition", cheese.composition)
                    putExtra("stages", cheese.stages)
                    putExtra("badgeColor", cheese.badgeColor)
                    putExtra("isArchived", cheese.isArchived)
                }

            fun parseExtraIntent(intent: Intent) =
                Cheese(
                    intent.getLongExtra("id", 0),
                    intent.getStringExtra("name") ?: "",
                    intent.getLongExtra("dateStart", 0),
                    intent.getLongExtra("date", 0),
                    intent.getStringExtra("recipe") ?: "",
                    intent.getStringExtra("comment") ?: "",
                    intent.getStringExtra("milk") ?: "",
                    intent.getStringExtra("composition") ?: "",
                    intent.getStringExtra("stages") ?: "",
                    intent.getIntExtra("badgeColor", 0),
                    false,
                    intent.getBooleanExtra("isArchived", false)
                )
        }
    }
}