package com.malec.store

interface ErrorHandler {
    fun handle(t: Throwable)
}