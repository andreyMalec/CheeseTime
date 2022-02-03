package com.malec.presentation.base

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.malec.presentation.R

open class AlertDialogBuilder(protected val context: Context) {
    private var onOkClick: OnClickListener? = null
    private var onCancelClick: OnClickListener? = null

    private lateinit var dialog: AlertDialog

    open fun setOnOkButtonClickListener(onClick: OnClickListener): AlertDialogBuilder {
        onOkClick = onClick

        return this
    }

    open fun setOnCancelButtonClickListener(onClick: OnClickListener): AlertDialogBuilder {
        onCancelClick = onClick

        return this
    }

    protected fun show(message: String, okTextResource: Int) {
        dialog = AlertDialog.Builder(context).apply {
            setTitle(message)
            setPositiveButton(okTextResource) { _, _ ->
                onOkClick?.onClick()
            }
            setNegativeButton(R.string.dialog_cancel) { _, _ ->
                dialog.cancel()
            }
            setOnCancelListener {
                onCancelClick?.onClick()
            }
        }.show()
        changeButtonColor(dialog)
    }

    protected fun changeButtonColor(dialog: AlertDialog) {
        val pButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val nButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        pButton.setTextColor(color)
        nButton.setTextColor(color)
    }

    fun interface OnClickListener {
        fun onClick()
    }
}