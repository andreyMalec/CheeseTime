package com.malec.presentation.unidirectional

import com.malec.store.ErrorHandler

interface BaseDependencies {
    fun getErrorHandler(): ErrorHandler
}