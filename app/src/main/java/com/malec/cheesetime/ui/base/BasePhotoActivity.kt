package com.malec.cheesetime.ui.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.ui.Screens.CAMERA
import com.malec.cheesetime.ui.Screens.STORAGE
import com.malec.cheesetime.ui.allertDialogBuilder.PhotoDeleteDialog

abstract class BasePhotoActivity : BaseActivity() {
    abstract val viewModel: BasePhotoViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION.code && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            viewModel.onPhotoDownloadClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.photoManageResult.observe(this, { message ->
            if (!message.isNullOrBlank()) {
                showMessage(message)
                viewModel.photoManageResult.value = null
            }
        })
    }

    protected fun checkOrRequestPermission(permission: Permission, onGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission.name
            ) == PackageManager.PERMISSION_GRANTED
        )
            onGranted()
        else
            requestPermissions(arrayOf(permission.name), permission.code)
    }

    protected fun deletePhotoDialog(photo: Photo?) {
        if (photo == null)
            return

        PhotoDeleteDialog(this).setOnOkButtonClickListener {
            viewModel.onPhotoDeleteClick(photo)
        }.show()
    }

    protected fun downloadPhoto(photo: Photo?) {
        if (photo == null)
            return

        viewModel.setDownloadedPhoto(photo)

        checkOrRequestPermission(STORAGE_PERMISSION) {
            viewModel.onPhotoDownloadClick()
        }
    }

    companion object {
        val CAMERA_PERMISSION =
            Permission(Manifest.permission.CAMERA, CAMERA)
        val STORAGE_PERMISSION =
            Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE)
    }

    data class Permission(
        val name: String,
        val code: Int
    )
}