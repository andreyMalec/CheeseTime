package com.malec.injection

interface ComponentOwnerLifecycle {
    fun onCreate()

    fun onDestroy()
}