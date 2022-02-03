package com.malec.tasklist.di

import javax.inject.Inject

class TaskListComponentProvider {

    @Inject
    lateinit var component: TaskListComponent

    companion object {
        var injectionFunction: (TaskListComponentProvider.() -> Unit)? = null

        private val instance = TaskListComponentProvider()

        @JvmStatic
        internal fun getInstance(): TaskListComponentProvider {
            injectionFunction?.invoke(instance)
            return instance
        }
    }
}