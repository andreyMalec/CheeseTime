package com.malec.cheesetime.ui.main

import android.content.ActivityNotFoundException
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
    containerId: Int
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
        val requestCode = when (screen.screenKey) {
            Screens.GALLERY -> Screens.CODE_GALLERY
            Screens.CAMERA -> Screens.CODE_CAMERA
            Screens.GOOGLE_LOGIN -> Screens.CODE_GOOGLE_LOGIN
            Screens.SCAN -> Screens.CODE_SCAN
            else -> -1
        }
        val activityIntent = screen.createIntent(activity)
        try {
            if (requestCode >= 0)
                activity.startActivityForResult(
                    activityIntent,
                    requestCode
                )
            else
                activity.startActivity(activityIntent, screen.startActivityOptions)
        } catch (e: ActivityNotFoundException) {
            unexistingActivity(screen, activityIntent)
        }
    }
}