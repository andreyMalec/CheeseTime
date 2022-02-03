package com.malec.cheesetime.coordinator

import com.github.terrakok.cicerone.Router
import com.malec.cheesedetail.CheeseDetailScreen
import com.malec.cheeselist.dependencies.CheeseListOutput

class CheeseListCoordinator(
    private val router: Router
) : CheeseListOutput {
    override fun openDetail(id: Long) {
        router.navigateTo(CheeseDetailScreen(id))
    }

    override fun onClickExit() {
        router.exit()
    }
}