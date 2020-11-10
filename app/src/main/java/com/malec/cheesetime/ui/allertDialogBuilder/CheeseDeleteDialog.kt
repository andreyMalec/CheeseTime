package com.malec.cheesetime.ui.allertDialogBuilder

import android.content.Context
import com.malec.cheesetime.R

class CheeseDeleteDialog(context: Context) : AlertDialogBuilder(context) {
    fun show(selectedCount: Int? = 1) {
        val message = if (selectedCount == 1)
            context.getString(R.string.delete_cheese_dialog_message)
        else
            context.getString(R.string.delete_selected_cheese_dialog_message, selectedCount)

        show(message, R.string.delete_dialog_ok)
    }

    override fun setOnOkButtonClickListener(function: () -> Unit): CheeseDeleteDialog {
        super.setOnOkButtonClickListener(function)

        return this
    }

    override fun setOnCancelButtonClickListener(function: () -> Unit): CheeseDeleteDialog {
        super.setOnCancelButtonClickListener(function)

        return this
    }
}