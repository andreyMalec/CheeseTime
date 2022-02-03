package com.malec.injection.callback

import com.malec.injection.ComponentOwner
import com.malec.injection.ComponentOwnerLifecycle
import com.malec.injection.ComponentsStore

internal class ComponentCallbackImpl(
    private val componentsStore: ComponentsStore
) : ComponentCallback {

    override fun <T> onAddInjection(componentOwner: ComponentOwner<T>) {
        val component = getComponent(componentOwner)
        componentOwner.inject(component)
    }

    override fun <T> onRemoveInjection(componentOwner: ComponentOwner<T>) {
        componentsStore.remove(componentOwner.getComponentKey())
    }

    fun <T> getComponent(owner: ComponentOwner<T>) = initOrGetComponent(owner)

    fun <T> clearComponent(componentOwner: ComponentOwner<T>) {
        componentsStore.remove(componentOwner.getComponentKey())
    }

    fun <T> getCustomOwnerLifecycle(owner: ComponentOwner<T>): ComponentOwnerLifecycle {
        return object : ComponentOwnerLifecycle {

            private var isInjected = false

            override fun onCreate() {
                if (!isInjected) {
                    onAddInjection(owner)
                    isInjected = true
                }
            }

            override fun onDestroy() {
                if (isInjected) {
                    onRemoveInjection(owner)
                    isInjected = false
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> initOrGetComponent(owner: ComponentOwner<T>): T {
        val ownerKey = owner.getComponentKey()

        return if (componentsStore.isExist(ownerKey)) {
            componentsStore[ownerKey] as T
        } else {
            owner.provideComponent().also { newComponent ->
                componentsStore.add(ownerKey, newComponent as Any)
            }
        }
    }
}