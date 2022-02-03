package com.malec.cheesedetail.di

import javax.inject.Inject

class CheeseDetailComponentProvider {

    @Inject
    lateinit var component: CheeseDetailComponent

    companion object {
        var injectionFunction: (CheeseDetailComponentProvider.() -> Unit)? = null

        private val instance = CheeseDetailComponentProvider()

        @JvmStatic
        internal fun getInstance(): CheeseDetailComponentProvider {
            injectionFunction?.invoke(instance)
            return instance
        }
    }
}