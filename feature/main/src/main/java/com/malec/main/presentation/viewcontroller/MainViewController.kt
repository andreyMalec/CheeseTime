package com.malec.main.presentation.viewcontroller

import com.malec.main.presentation.store.MainAction
import com.malec.main.presentation.store.MainState
import com.malec.main.presentation.store.MainStore
import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.unidirectional.BaseViewController

class MainViewController(
    store: MainStore
) : BaseViewController<MainState, MainAction, BaseView<MainState>>(store) {

    override fun firstViewAttach() {
        dispatchAction(MainAction.Init)
    }
}