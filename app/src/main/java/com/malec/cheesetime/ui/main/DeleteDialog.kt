package com.malec.cheesetime.ui.main

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.malec.cheesetime.R

class DeleteDialog(private val context: Context) {
    private lateinit var onOkClick: () -> Unit

    fun showCheeseDialog(selectedCount: Int? = 1) {
        val message = if (selectedCount == 1)
            context.getString(R.string.delete_cheese_dialog_message)
        else
            context.getString(R.string.delete_selected_cheese_dialog_message, selectedCount)

        show(message)
    }

    fun showTaskDialog(selectedCount: Int = 1) {
        val message = if (selectedCount == 1)
            context.getString(R.string.delete_task_dialog_message)
        else
            context.getString(R.string.delete_selected_task_dialog_message, selectedCount)

        show(message)
    }

    fun setOnOkButtonClickListener(function: () -> Unit): DeleteDialog {
        onOkClick = function

        return this
    }

    private fun show(message: String) {
        AlertDialog.Builder(context).apply {
            setMessage(message)
            setPositiveButton(R.string.delete_dialog_ok) { _, _ ->
                onOkClick
            }
            setNegativeButton(R.string.delete_dialog_cancel, null)
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