package com.malec.cheesetime.ui.allertDialogBuilder

import android.content.Context
import com.malec.cheesetime.R
import com.malec.presentation.base.AlertDialogBuilder

class PhotoDeleteDialog(context: Context) : AlertDialogBuilder(context) {
    fun show() {
        show(context.getString(R.string.delete_photo_dialog_message), R.string.delete_dialog_ok)
    }

    override fun setOnOkButtonClickListener(onClick: OnClickListener): PhotoDeleteDialog {
        super.setOnOkButtonClickListener(onClick)

        return this
    }

    override fun setOnCancelButtonClickListener(onClick: OnClickListener): PhotoDeleteDialog {
        super.setOnCancelButtonClickListener(onClick)

        return this
    }
}