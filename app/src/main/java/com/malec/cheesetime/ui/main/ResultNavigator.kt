package com.malec.cheesetime.ui.main

import android.content.ActivityNotFoundException
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.Forward
import com.github.terrakok.cicerone.androidx.*
import com.malec.cheesetime.R
import com.malec.cheesetime.ui.Screens
import com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments.FullscreenPhotoFragment

class ResultNavigator(
    activity: FragmentActivity,
    containerId: Int,
    private val launchers: Map<String, ActivityResultLauncher<Void>>
) : AppNavigator(activity, containerId) {
    override fun forward(command: Forward) {
        when (val screen = command.screen as AppScreen) {
            is ActivityScreen -> {
                checkAndStartActivity(screen)
            }
            is FragmentScreen -> {
                val type = if (command.clearContainer) TransactionInfo.Type.REPLACE
                else TransactionInfo.Type.ADD
                commitNewFragmentScreen(screen, type, true)
            }
        }
    }

    override fun setupFragmentTransaction(
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment?
    ) {
        super.setupFragmentTransaction(fragmentTransaction, currentFragment, nextFragment)

        if (nextFragment is FullscreenPhotoFragment || currentFragment is FullscreenPhotoFragment)
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
    }

    private fun checkAndStartActivity(screen: ActivityScreen) {
        val activityIntent = screen.createIntent(activity)
        try {
            when (screen.screenKey) {
                Screens.GALLERY -> launchers[Screens.GALLERY]?.launch(null)
                Screens.CAMERA -> launchers[Screens.CAMERA]?.launch(null)
                Screens.GOOGLE_LOGIN -> launchers[Screens.GOOGLE_LOGIN]?.launch(null)
                Screens.SCAN -> launchers[Screens.SCAN]?.launch(null)
                else -> activity.startActivity(activityIntent, screen.startActivityOptions)
            }
        } catch (e: ActivityNotFoundException) {
            unexistingActivity(screen, activityIntent)
        }
    }
}