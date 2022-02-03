package com.malec.cheesetime.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.zxing.integration.android.IntentIntegrator
import com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments.CheeseManageFragment
import com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments.FullscreenPhotoFragment
import com.malec.cheesetime.ui.settings.SettingsActivity
import com.malec.cheesetime.util.CameraIntentCreator
import com.malec.main.presentation.view.MainActivity

object Screens {
    const val CODE_GALLERY = 10
    const val CODE_CAMERA = 11
    const val CODE_GOOGLE_LOGIN = 12
    const val CODE_STORAGE = 5
    const val CODE_SCAN = 0x0000c0de

    const val GALLERY = "Gallery"
    const val CAMERA = "Camera"

    const val TASK_LIST = "TaskList"
    const val CHEESE_LIST = "CheeseList"
    const val REPORTS = "Reports"

    const val SCAN = "Scan"

    fun main() = ActivityScreen {
        it.createIntent<MainActivity>()
    }

    fun galleryPick() = ActivityScreen(GALLERY) {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    object GalleryPickScreen : ActivityScreen {
        override val screenKey = GALLERY
        override fun createIntent(context: Context) =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    fun cameraPick() = ActivityScreen(CAMERA) {
        CameraIntentCreator(it).create()
    }

    object CameraPickScreen : ActivityScreen {
        override val screenKey = CAMERA
        override fun createIntent(context: Context) =
            CameraIntentCreator(context).create()
    }

    fun scan() = ActivityScreen(SCAN) {
        IntentIntegrator(it as Activity).apply {
            setOrientationLocked(false)
            setDesiredBarcodeFormats(IntentIntegrator.CODE_128)
        }.createScanIntent()
    }

    object ScanScreen : ActivityScreen {
        override val screenKey = SCAN
        override fun createIntent(context: Context): Intent =
            IntentIntegrator(context as Activity).apply {
                setOrientationLocked(false)
                setDesiredBarcodeFormats(IntentIntegrator.CODE_128)
            }.createScanIntent()
    }

    fun settings() = ActivityScreen {
        it.createIntent<SettingsActivity>()
    }

    fun cheeseManageFragment() = FragmentScreen {
        CheeseManageFragment()
    }

    fun fullscreenPhoto() = FragmentScreen {
        FullscreenPhotoFragment()
    }
}