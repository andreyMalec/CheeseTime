package com.malec.cheesetime

import android.Manifest
import com.malec.cheesetime.ui.Screens

data class Permission(
    val name: String,
    val code: Int
)

object Permissions {
    val CAMERA =
        Permission(Manifest.permission.CAMERA, Screens.CODE_CAMERA)
    val STORAGE =
        Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Screens.CODE_STORAGE)
}

fun interface PermissionListener {
    fun onGranted()
}