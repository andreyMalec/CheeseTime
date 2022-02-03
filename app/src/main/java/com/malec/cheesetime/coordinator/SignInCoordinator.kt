package com.malec.cheesetime.coordinator

import com.github.terrakok.cicerone.Router
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.malec.signin.GoogleLoginScreen
import com.malec.signin.dependencies.SignInOutput
import com.malec.tabs.TabsScreen

class SignInCoordinator(
    private val router: Router,
    private val client: GoogleSignInClient
) : SignInOutput {
    override fun onGoogleLogin() {
        router.navigateTo(GoogleLoginScreen(client))
    }

    override fun nextScreen() {
        router.newRootScreen(TabsScreen)
    }

    override fun exit() {
        router.exit()
    }
}