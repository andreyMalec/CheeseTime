package com.malec.cheesetime.resultContract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.malec.cheesetime.util.BitmapDecoder
import com.malec.domain.data.PhotoResult

class PhotoResultContract(
    private val screen: ActivityScreen,
    private val bitmapDecoder: BitmapDecoder
) : ActivityResultContract<Any, Any>() {
    override fun createIntent(context: Context, input: Any?): Intent {
        return screen.createIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Any = when (resultCode) {
        Activity.RESULT_OK -> {
            val bitmap = if (intent != null && intent.data != null)
                bitmapDecoder.fromUri(intent.data)
            else
                bitmapDecoder.fromCamera()
            bitmap?.let {
                PhotoResult(it)
            } ?: Any()
        }
        else -> Any()
    }
}