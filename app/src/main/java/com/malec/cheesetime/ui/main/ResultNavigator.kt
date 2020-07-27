package com.malec.cheesetime.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.malec.cheesetime.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Forward

@ExperimentalCoroutinesApi
class ResultNavigator(
    activity: FragmentActivity,
    fragmentManager: FragmentManager,
    containerId: Int
) : SupportAppNavigator(activity, fragmentManager, containerId) {
    override fun activityForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)

        // Start activity for result
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
        } else {
            fragmentForward(command)
        }
    }

    private fun checkAndStartActivity(
        screen: SupportAppScreen,
        activityIntent: Intent,
        options: Bundle?
    ) {
        val requestCode = when (screen) {
            is Screens.GalleryPickScreen -> Screens.GalleryPickScreen.requestCode
            is Screens.CameraPickScreen -> Screens.CameraPickScreen.requestCode
            else -> -1
        }

        // Check if we can start activity
        if (activityIntent.resolveActivity(activity.packageManager) != null) {
            if (requestCode >= 0)
                activity.startActivityForResult(activityIntent, requestCode)
            else
                activity.startActivity(activityIntent, options)
        } else {
            unexistingActivity(screen, activityIntent)
        }
    }
}