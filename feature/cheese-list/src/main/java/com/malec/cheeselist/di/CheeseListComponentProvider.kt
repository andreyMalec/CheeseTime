package com.malec.cheeselist.di

import javax.inject.Inject

class CheeseListComponentProvider {

    @Inject
    lateinit var component: CheeseListComponent

    companion object {
        var injectionFunction: (CheeseListComponentProvider.() -> Unit)? = null

        private val instance = CheeseListComponentProvider()

        @JvmStatic
        internal fun getInstance(): CheeseListComponentProvider {
            injectionFunction?.invoke(instance)
            return instance
        }
    }
}