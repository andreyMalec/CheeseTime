package com.malec.presentation.dialog

import android.content.Context
import com.malec.presentation.R
import com.malec.presentation.base.AlertDialogBuilder

class CheeseDeleteDialog(context: Context) : AlertDialogBuilder(context) {
    fun show(selectedCount: Int? = 1) {
        val message = if (selectedCount == 1)
            context.getString(R.string.delete_cheese_dialog_message)
        else
            context.getString(R.string.delete_selected_cheese_dialog_message, selectedCount)

        show(message, R.string.delete_dialog_ok)
    }

    override fun setOnOkButtonClickListener(onClick: OnClickListener): CheeseDeleteDialog {
        super.setOnOkButtonClickListener(onClick)

        return this
    }

    override fun setOnCancelButtonClickListener(onClick: OnClickListener): CheeseDeleteDialog {
        super.setOnCancelButtonClickListener(onClick)

        return this
    }
}