package com.malec.presentation.unidirectional

interface BaseView<State> {
    fun renderState(state: State)
}