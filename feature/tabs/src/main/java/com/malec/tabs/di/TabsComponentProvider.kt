package com.malec.tabs.di

import javax.inject.Inject

class TabsComponentProvider {

    @Inject
    lateinit var component: TabsComponent

    companion object {
        var injectionFunction: (TabsComponentProvider.() -> Unit)? = null

        private val instance = TabsComponentProvider()

        @JvmStatic
        internal fun getInstance(): TabsComponentProvider {
            injectionFunction?.invoke(instance)
            return instance
        }
    }
}