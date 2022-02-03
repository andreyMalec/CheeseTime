package com.malec.presentation.navigation

import android.content.ActivityNotFoundException
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.Forward
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen

class ResultNavigator(
    activity: FragmentActivity,
    containerId: Int,
    private val launchers: Map<String, ActivityResultLauncher<*>>
) : AppNavigator(activity, containerId) {
    private val containers = mutableMapOf<String, Int>()

    override fun forward(command: Forward) {
        when (val screen = command.screen) {
            is ActivityScreen -> {
                checkAndStartActivity(screen)
            }
            is FragmentScreen -> {
                commitNewFragmentScreen(screen, true)
            }
        }
    }

    fun setContainers(containers: Map<String, Int>) {
        this.containers.clear()
        this.containers.putAll(containers)
    }

    override fun setupFragmentTransaction(
        screen: FragmentScreen,
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment
    ) {
        super.setupFragmentTransaction(screen, fragmentTransaction, currentFragment, nextFragment)

//        if (nextFragment is FullscreenPhotoFragment || currentFragment is FullscreenPhotoFragment)
//            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
    }

    private fun checkAndStartActivity(screen: ActivityScreen) {
        val activityIntent = screen.createIntent(activity)
        try {
            val launcher = launchers[screen.screenKey]
            if (launcher != null)
                launcher.launch(null)
            else
                activity.startActivity(activityIntent, screen.startActivityOptions)
        } catch (e: ActivityNotFoundException) {
            unexistingActivity(screen, activityIntent)
        }
    }

    override fun commitNewFragmentScreen(
        screen: FragmentScreen,
        addToBackStack: Boolean
    ) {
        val container = containers[screen.screenKey] ?: containerId
        val fragment = screen.createFragment(fragmentFactory)
        val transaction = fragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        setupFragmentTransaction(
            screen,
            transaction,
            fragmentManager.findFragmentById(container),
            fragment
        )
        if (screen.clearContainer) {
            transaction.replace(container, fragment, screen.screenKey)
        } else {
            transaction.add(container, fragment, screen.screenKey)
        }
        if (addToBackStack) {
            transaction.addToBackStack(screen.screenKey)
            localStackCopy.add(screen.screenKey)
        }
        transaction.commit()
    }
}