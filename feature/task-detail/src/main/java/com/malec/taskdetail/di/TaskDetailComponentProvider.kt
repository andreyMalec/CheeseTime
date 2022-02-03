package com.malec.taskdetail.di

import javax.inject.Inject

class TaskDetailComponentProvider {

    @Inject
    lateinit var component: TaskDetailComponent

    companion object {
        var injectionFunction: (TaskDetailComponentProvider.() -> Unit)? = null

        private val instance = TaskDetailComponentProvider()

        @JvmStatic
        internal fun getInstance(): TaskDetailComponentProvider {
            injectionFunction?.invoke(instance)
            return instance
        }
    }
}