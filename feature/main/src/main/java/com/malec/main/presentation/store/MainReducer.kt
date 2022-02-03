package com.malec.main.presentation.store

import com.malec.store.Reducer

class MainReducer : Reducer<MainState, MainAction> {
    override fun reduce(state: MainState, action: MainAction): MainState {
        return when (action) {
            MainAction.Init -> {
                state.copy(
                    loaded = true
                )
            }

            else -> state.copy()
        }
    }
}