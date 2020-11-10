package com.malec.cheesetime.ui.allertDialogBuilder

import android.content.Context
import com.malec.cheesetime.R

class PhotoDeleteDialog(context: Context) : AlertDialogBuilder(context) {
    fun show() {
        show(context.getString(R.string.delete_photo_dialog_message), R.string.delete_dialog_ok)
    }

    override fun setOnOkButtonClickListener(function: () -> Unit): PhotoDeleteDialog {
        super.setOnOkButtonClickListener(function)

        return this
    }

    override fun setOnCancelButtonClickListener(function: () -> Unit): PhotoDeleteDialog {
        super.setOnCancelButtonClickListener(function)

        return this
    }
}