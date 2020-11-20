package com.malec.cheesetime.ui

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.PhotoF
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.repo.UserRepo.Companion.googleSignInClient
import com.malec.cheesetime.ui.login.LoginActivity
import com.malec.cheesetime.ui.main.MainActivity
import com.malec.cheesetime.ui.main.cheese.cheeseList.CheeseListFragment
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageActivity
import com.malec.cheesetime.ui.main.cheese.cheeseManage.fullscreenPhoto.FullscreenPhotoActivity
import com.malec.cheesetime.ui.main.report.ReportsFragment
import com.malec.cheesetime.ui.main.task.taskList.TaskListFragment
import com.malec.cheesetime.ui.main.task.taskManage.TaskManageActivity
import com.malec.cheesetime.ui.settings.SettingsActivity
import com.malec.cheesetime.util.CameraIntentCreator
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    private val gson = Gson()

    const val GALLERY = GalleryPickScreen.requestCode
    const val CAMERA = CameraPickScreen.requestCode
    const val STORAGE = 5

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

    object GalleryPickScreen : SupportAppScreen() {
        const val requestCode = 10

        override fun getActivityIntent(context: Context): Intent? {
            return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }
    }

    object CameraPickScreen : SupportAppScreen() {
        const val requestCode = 11

        override fun getActivityIntent(context: Context): Intent? {
            return CameraIntentCreator(context).create()
        }
    }

    object SettingsScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    object GoogleLoginScreen : SupportAppScreen() {
        const val requestCode = 12

        override fun getActivityIntent(context: Context): Intent? {
            return googleSignInClient(context)?.signInIntent
        }
    }

    class TaskManageScreen(private var task: Task? = null) : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return task?.let { createExtraIntent(context, it) }
                ?: Intent(context, TaskManageActivity::class.java)
        }

        companion object {
            fun createExtraIntent(context: Context, task: Task) =
                Intent(context, TaskManageActivity::class.java).putExtra("data", gson.toJson(task))

            fun parseExtraIntent(intent: Intent): Task? =
                gson.fromJson(intent.getStringExtra("data"), Task::class.java)
        }
    }

    class CheeseManageScreen(private var cheese: Cheese? = null) : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return cheese?.let { createExtraIntent(context, it) }
                ?: Intent(context, CheeseManageActivity::class.java)
        }

        companion object {
            fun createExtraIntent(context: Context, cheese: Cheese) =
                Intent(context, CheeseManageActivity::class.java).putExtra(
                    "data",
                    gson.toJson(cheese)
                )

            fun parseExtraIntent(intent: Intent): Cheese? =
                gson.fromJson(intent.getStringExtra("data"), Cheese::class.java)
        }
    }

    class FullscreenPhotoScreen(
        private val photo: PhotoF,
        private val transitionOptions: Pair<View, String>
    ) : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return createExtraIntent(context, photo)
        }

        fun getOptions() = transitionOptions

        companion object {
            const val requestCode = 15

            fun createExtraIntent(context: Context, photo: PhotoF) =
                Intent(context, FullscreenPhotoActivity::class.java).putExtra(
                    "data",
                    gson.toJson(photo)
                )

            fun parseExtraIntent(intent: Intent): PhotoF? =
                gson.fromJson(intent.getStringExtra("data"), PhotoF::class.java)
        }
    }
}