package com.malec.injection.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.malec.injection.ComponentOwner

internal class ActivityLifecycleCallback(
    private val componentCallback: ComponentCallback
) : Application.ActivityLifecycleCallbacks {

    private companion object {
        const val IS_FIRST_LAUNCH = "com.malec.injection.callback.IS_FIRST_LAUNCH"
    }

    private fun isFirstLaunch(outState: Bundle?): Boolean =
        outState?.getBoolean(IS_FIRST_LAUNCH, true) ?: true

    private fun setFirstLaunch(outState: Bundle, isFirstLaunch: Boolean) {
        outState.putBoolean(IS_FIRST_LAUNCH, isFirstLaunch)
    }

    private fun addInjection(activity: Activity, outState: Bundle?) {
        if (activity is ComponentOwner<*>) {
            if (isFirstLaunch(outState)) {
                componentCallback.onRemoveInjection(activity)
            }
            componentCallback.onAddInjection(activity)
        }

        (activity as? AppCompatActivity)
            ?.supportFragmentManager
            ?.registerFragmentLifecycleCallbacks(
                FragmentLifecycleCallback(
                    componentCallback
                ), true
            )
    }

    private fun removeInjectionIfNeed(activity: Activity) {
        if (activity is ComponentOwner<*> && activity.isFinishing) {
            componentCallback.onRemoveInjection(activity)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        addInjection(activity, savedInstanceState)
    }

    override fun onActivityPaused(activity: Activity) {
        removeInjectionIfNeed(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        setFirstLaunch(outState, isFirstLaunch = false)
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}
}