package com.malec.cheesetime.ui

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.zxing.integration.android.IntentIntegrator
import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.Task
import com.malec.cheesetime.repo.UserRepo.Companion.googleSignInClient
import com.malec.cheesetime.ui.login.LoginActivity
import com.malec.cheesetime.ui.main.MainActivity
import com.malec.cheesetime.ui.main.cheese.cheeseList.CheeseListFragment
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageActivity
import com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments.CheeseManageFragment
import com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments.FullscreenPhotoFragment
import com.malec.cheesetime.ui.main.report.ReportsFragment
import com.malec.cheesetime.ui.main.task.taskList.TaskListFragment
import com.malec.cheesetime.ui.main.task.taskManage.TaskManageActivity
import com.malec.cheesetime.ui.settings.SettingsActivity
import com.malec.cheesetime.util.CameraIntentCreator

object Screens {
    const val CODE_GALLERY = 10
    const val CODE_CAMERA = 11
    const val CODE_GOOGLE_LOGIN = 12
    const val CODE_STORAGE = 5
    const val CODE_SCAN = 0x0000c0de

    const val GALLERY = "Gallery"
    const val CAMERA = "Camera"
    const val GOOGLE_LOGIN = "GoogleLogin"

    const val TASK_LIST = "TaskList"
    const val CHEESE_LIST = "CheeseList"
    const val REPORTS = "Reports"

    const val SCAN = "Scan"

    fun taskList() = FragmentScreen(TASK_LIST) {
        TaskListFragment()
    }

    fun cheeseList() = FragmentScreen(CHEESE_LIST) {
        CheeseListFragment()
    }

    fun reports() = FragmentScreen(REPORTS) {
        ReportsFragment()
    }

    fun main() = ActivityScreen {
        it.createIntent<MainActivity>()
    }

    fun login() = ActivityScreen {
        it.createIntent<LoginActivity>()
    }

    fun galleryPick() = ActivityScreen(GALLERY) {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    fun cameraPick() = ActivityScreen(CAMERA) {
        CameraIntentCreator(it).create()
    }

    fun scan() = ActivityScreen(SCAN) {
        IntentIntegrator(it as Activity).apply {
            setOrientationLocked(false)
            setDesiredBarcodeFormats(IntentIntegrator.CODE_128)
        }.createScanIntent()
    }

    fun settings() = ActivityScreen {
        it.createIntent<SettingsActivity>()
    }

    fun googleLogin() = ActivityScreen(GOOGLE_LOGIN) {
        googleSignInClient(it).signInIntent
    }

    fun taskManage(task: Task? = null) = ActivityScreen {
        it.createIntent<TaskManageActivity>(task)
    }

    fun cheeseManage(cheese: Cheese? = null) = ActivityScreen {
        it.createIntent<CheeseManageActivity>(cheese)
    }

    fun cheeseManageFragment() = FragmentScreen {
        CheeseManageFragment()
    }

    fun fullscreenPhoto() = FragmentScreen {
        FullscreenPhotoFragment()
    }
}