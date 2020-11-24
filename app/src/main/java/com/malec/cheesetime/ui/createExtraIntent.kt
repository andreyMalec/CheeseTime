package com.malec.cheesetime.ui

import android.content.Context
import android.content.Intent
import com.google.gson.Gson

inline fun <reified T> Context.createIntent(any: Any? = null) =
    Intent(this, T::class.java).apply {
        any?.let {
            putExtra("data", Gson().toJson(it))
        }
    }

inline fun <reified T> parseExtraIntent(intent: Intent): T? =
    Gson().fromJson(intent.getStringExtra("data"), T::class.java)