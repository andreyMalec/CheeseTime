package com.malec.cheesetime.coordinator

import com.github.terrakok.cicerone.Router
import com.malec.cheesedetail.dependencies.CheeseDetailOutput

class CheeseDetailCoordinator(
    private val router: Router
) : CheeseDetailOutput {
    override fun exit() {
        router.exit()
    }
}