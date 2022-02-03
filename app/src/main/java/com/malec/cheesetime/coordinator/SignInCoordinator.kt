package com.malec.cheesetime.coordinator

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.malec.signin.dependencies.SignInOutput
import com.malec.tabs.TabsScreen

class SignInCoordinator(
    private val router: Router
) : SignInOutput {
    override fun onGoogleLogin() {
        router.navigateTo(object : ActivityScreen {
            override fun createIntent(context: Context): Intent {
                return Intent(Intent.ACTION_VIEW, Uri.parse("https://google.ru"))
            }
        })
    }

    override fun nextScreen() {
        router.newRootScreen(TabsScreen)
    }

    override fun exit() {
        router.exit()
    }
}