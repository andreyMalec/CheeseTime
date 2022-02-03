package com.malec.cheesetime.resultContract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.malec.cheesetime.ui.Screens
import com.malec.domain.data.GoogleLoginResult

class GoogleLoginResultContract(private val screen: Screens.GoogleLoginScreen) :
    ActivityResultContract<Any, Any>() {
    override fun createIntent(context: Context, input: Any?): Intent {
        return screen.createIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Any = when (resultCode) {
        Activity.RESULT_OK -> intent?.let {
            GoogleLoginResult(it)
        } ?: Any()
        else -> Any()
    }
}