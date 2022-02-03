package com.malec.injection.callback

import com.malec.injection.ComponentOwner

interface ComponentCallback {
    fun <T> onAddInjection(componentOwner: ComponentOwner<T>)

    fun <T> onRemoveInjection(componentOwner: ComponentOwner<T>)
}