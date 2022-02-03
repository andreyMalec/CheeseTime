package com.malec.tabs.presentation.view

import android.content.Context
import com.malec.presentation.base.AlertDialogBuilder
import com.malec.tabs.R

class LogoutDialog(context: Context) : AlertDialogBuilder(context) {
    fun show() {
        show(context.getString(R.string.logout_dialog_message), R.string.logout_dialog_ok)
    }

    override fun setOnOkButtonClickListener(onClick: OnClickListener): LogoutDialog {
        super.setOnOkButtonClickListener(onClick)

        return this
    }

    override fun setOnCancelButtonClickListener(onClick: OnClickListener): LogoutDialog {
        super.setOnCancelButtonClickListener(onClick)

        return this
    }
}