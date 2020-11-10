package com.malec.cheesetime.ui.allertDialogBuilder

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.malec.cheesetime.R

open class AlertDialogBuilder(protected val context: Context) {
    private var onOkClick: (() -> Unit)? = null
    private var onCancelClick: (() -> Unit)? = null

    private lateinit var dialog: AlertDialog

    open fun setOnOkButtonClickListener(function: () -> Unit): AlertDialogBuilder {
        onOkClick = function

        return this
    }

    open fun setOnCancelButtonClickListener(function: () -> Unit): AlertDialogBuilder {
        onCancelClick = function

        return this
    }

    protected fun show(message: String, okTextResource: Int) {
        dialog = AlertDialog.Builder(context).apply {
            setTitle(message)
            setPositiveButton(okTextResource) { _, _ ->
                onOkClick?.invoke()
            }
            setNegativeButton(R.string.dialog_cancel) { _, _ ->
                dialog.dismiss()
            }
            setOnDismissListener {
                onCancelClick?.invoke()
            }
        }.show()
        changeButtonColor(dialog)
    }

    private fun changeButtonColor(dialog: AlertDialog) {
        val pButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val nButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        pButton.setTextColor(color)
        nButton.setTextColor(color)
    }
}