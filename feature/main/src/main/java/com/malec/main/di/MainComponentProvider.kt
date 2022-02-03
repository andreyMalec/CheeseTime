package com.malec.main.di

class MainComponentProvider {
    lateinit var component: MainComponent

    companion object {
        var injectionFunction: (MainComponentProvider.() -> Unit)? = null

        private val instance = MainComponentProvider()

        internal fun newInstance(): MainComponentProvider {
            injectionFunction?.invoke(instance)
            return instance
        }
    }
}