package com.malec.cheesetime.ui.main

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.malec.cheesetime.R

class AlertDialogBuilder(private val context: Context) {
    private lateinit var onOkClick: () -> Unit
    private lateinit var onCancelClick: () -> Unit

    fun showCheeseDialog(selectedCount: Int? = 1) {
        val message = if (selectedCount == 1)
            context.getString(R.string.delete_cheese_dialog_message)
        else
            context.getString(R.string.delete_selected_cheese_dialog_message, selectedCount)

        show(message, R.string.delete_dialog_ok)
    }

    fun showTaskDialog(selectedCount: Int = 1) {
        val message = if (selectedCount == 1)
            context.getString(R.string.delete_task_dialog_message)
        else
            context.getString(R.string.delete_selected_task_dialog_message, selectedCount)

        show(message, R.string.delete_dialog_ok)
    }

    fun showPhotoDeleteDialog() {
        show(context.getString(R.string.delete_photo_dialog_message), R.string.delete_dialog_ok)
    }

    fun showLogoutDialog() {
        show(context.getString(R.string.logout_dialog_message), R.string.logout_dialog_ok)
    }

    fun setOnOkButtonClickListener(function: () -> Unit): AlertDialogBuilder {
        onOkClick = function

        return this
    }

    fun setOnCancelButtonClickListener(function: () -> Unit): AlertDialogBuilder {
        onCancelClick = function

        return this
    }

    private fun show(message: String, okTextResource: Int) {
        AlertDialog.Builder(context).apply {
            setTitle(message)
            setPositiveButton(okTextResource) { _, _ ->
                onOkClick()
            }
            setNegativeButton(R.string.dialog_cancel) { _, _ ->
                onCancelClick()
            }
        }.show().also {
            changeButtonColor(it)
        }
    }

    private fun changeButtonColor(dialog: AlertDialog) {
        val pButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val nButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        pButton.setTextColor(color)
        nButton.setTextColor(color)
    }
}