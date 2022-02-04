package com.malec.cheesedetail.presentation.store.actionhandler

import com.malec.cheesedetail.dependencies.CheeseDetailOutput
import com.malec.cheesedetail.presentation.store.CheeseDetailAction
import com.malec.cheesedetail.presentation.store.CheeseDetailState
import com.malec.store.ActionHandler

class BackActionHandler(
    private val output: CheeseDetailOutput
) : ActionHandler<CheeseDetailState, CheeseDetailAction>(
    requirement = { action -> action is CheeseDetailAction.Back },
    handler = { _, _ ->
        output.exit()
    }
)