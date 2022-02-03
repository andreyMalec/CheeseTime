package com.malec.injection

import android.app.Application
import com.malec.injection.callback.ActivityLifecycleCallback
import com.malec.injection.callback.ComponentCallbackImpl

class InjectionHolder {
    companion object {
        @JvmStatic
        val instance by lazy {
            InjectionHolder()
        }

        fun init(app: Application) {
            app.registerActivityLifecycleCallbacks(
                ActivityLifecycleCallback(
                    instance.componentCallback
                )
            )
        }
    }

    private val componentsStore by lazy { ComponentsStore() }

    private val componentCallback: ComponentCallbackImpl by lazy {
        ComponentCallbackImpl(
            componentsStore
        )
    }

    fun removeComponent(componentClass: Class<*>) = componentsStore.remove(componentClass)

    fun <T> clearComponent(owner: ComponentOwner<T>) = componentCallback.clearComponent(owner)

    fun <T> getComponent(owner: ComponentOwner<T>): T = componentCallback.getComponent(owner)

    fun <T> getComponentOwnerLifeCycle(componentOwner: ComponentOwner<T>): ComponentOwnerLifecycle =
        componentCallback.getCustomOwnerLifecycle(componentOwner)

    @JvmOverloads
    fun <T> findComponent(
        componentClass: Class<T>,
        componentBuilder: (() -> T)? = null
    ): T = componentsStore.findComponent(componentClass, componentBuilder)
}