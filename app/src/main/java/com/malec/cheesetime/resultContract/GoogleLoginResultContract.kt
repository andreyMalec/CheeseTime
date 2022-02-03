package com.malec.cheesetime.resultContract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.malec.domain.data.GoogleLoginResult
import com.malec.signin.GoogleLoginScreen

class GoogleLoginResultContract(private val screen: GoogleLoginScreen) :
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