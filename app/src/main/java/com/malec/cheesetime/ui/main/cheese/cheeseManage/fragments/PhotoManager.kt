package com.malec.cheesetime.ui.main.cheese.cheeseManage.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.malec.cheesetime.Permission
import com.malec.cheesetime.PermissionListener
import com.malec.cheesetime.Permissions
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.ui.allertDialogBuilder.PhotoDeleteDialog
import com.malec.cheesetime.ui.main.cheese.cheeseManage.CheeseManageViewModel
import javax.inject.Inject

abstract class PhotoManager : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract val viewModel: CheeseManageViewModel

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) requireActivity().onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Permissions.STORAGE.code && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            viewModel.onPhotoDownloadClick()

        if (requestCode == Permissions.CAMERA.code && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            viewModel.onAttachFromCamera()
    }

    fun deletePhotoDialog(context: Context, photo: Photo?) {
        if (photo == null)
            return

        PhotoDeleteDialog(context).setOnOkButtonClickListener {
            viewModel.onPhotoDeleteClick(photo)
        }.show()
    }

    fun downloadPhoto(photo: Photo?) {
        if (photo == null)
            return

        viewModel.setDownloadedPhoto(photo)

        checkOrRequestPermission(Permissions.STORAGE) {
            viewModel.onPhotoDownloadClick()
        }
    }

    protected fun checkOrRequestPermission(permission: Permission, listener: PermissionListener) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                permission.name
            ) == PackageManager.PERMISSION_GRANTED
        )
            listener.onGranted()
        else
            requestPermissions(arrayOf(permission.name), permission.code)
    }
}