package com.malec.cheesetime.resultContract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.zxing.integration.android.IntentIntegrator
import com.malec.cheesetime.ui.Screens
import com.malec.domain.data.ScanResult

class ScanResultContract(private val screen: Screens.ScanScreen) :
    ActivityResultContract<Any, Any>() {
    override fun createIntent(context: Context, input: Any?): Intent {
        return screen.createIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Any = when (resultCode) {
        Activity.RESULT_OK -> {
            ScanResult(IntentIntegrator.parseActivityResult(resultCode, intent).contents)
        }
        else -> Any()
    }
}