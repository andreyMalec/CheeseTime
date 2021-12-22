package com.malec.cheesetime.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.github.terrakok.cicerone.androidx.ActivityScreen

class ResultContract(private val screen: ActivityScreen) : ActivityResultContract<Void, Intent?>() {
    override fun createIntent(context: Context, input: Void?): Intent {
        return screen.createIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> intent
    }
}