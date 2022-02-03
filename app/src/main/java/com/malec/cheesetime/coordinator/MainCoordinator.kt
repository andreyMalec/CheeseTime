package com.malec.cheesetime.coordinator

import com.github.terrakok.cicerone.Router
import com.malec.main.dependencies.MainOutput
import com.malec.signin.SignInScreen
import com.malec.tabs.TabsScreen

class MainCoordinator(
    private val router: Router
) : MainOutput {
    override fun login() {
        router.newRootScreen(SignInScreen)
    }

    override fun main() {
        router.newRootScreen(TabsScreen)
    }

    override fun exit() {
        router.exit()
    }
}