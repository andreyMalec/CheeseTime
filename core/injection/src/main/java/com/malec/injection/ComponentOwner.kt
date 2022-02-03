package com.malec.injection

interface ComponentOwner<Component> {
    fun provideComponent(): Component

    fun inject(component: Component)

    fun getComponentKey(): String = javaClass.toString()
}