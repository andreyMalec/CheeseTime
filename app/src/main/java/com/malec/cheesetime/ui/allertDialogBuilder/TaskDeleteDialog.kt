package com.malec.cheesetime.ui.allertDialogBuilder

import android.content.Context
import com.malec.cheesetime.R

class TaskDeleteDialog(context: Context) : AlertDialogBuilder(context) {
    fun show(selectedCount: Int = 1) {
        val message = if (selectedCount == 1)
            context.getString(R.string.delete_task_dialog_message)
        else
            context.getString(R.string.delete_selected_task_dialog_message, selectedCount)

        show(message, R.string.delete_dialog_ok)
    }

    override fun setOnOkButtonClickListener(function: () -> Unit): TaskDeleteDialog {
        super.setOnOkButtonClickListener(function)

        return this
    }

    override fun setOnCancelButtonClickListener(function: () -> Unit): TaskDeleteDialog {
        super.setOnCancelButtonClickListener(function)

        return this
    }
}