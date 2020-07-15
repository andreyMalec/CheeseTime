package com.malec.cheesetime.ui.main.dialog

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.malec.cheesetime.R
import com.malec.cheesetime.util.CheeseCreator


class FilterDialog(
    private val target: DialogListener,
    private val title: String,
    private val helper: String
) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_filter, container, false)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        v.findViewById<TextView>(R.id.titleText).text = title
        val inputEditText = v.findViewById<TextView>(R.id.inputEditText)
        val inputLayout = v.findViewById<TextInputLayout>(R.id.inputLayout)
        inputEditText.doAfterTextChanged {
            if (inputLayout.error != null)
                inputLayout.error = null
        }
        inputLayout.helperText = helper

        if (helper.isNotBlank())
            inputEditText.inputType = InputType.TYPE_CLASS_DATETIME

        v.findViewById<Button>(R.id.okButton).setOnClickListener {
            val result = v.findViewById<EditText>(R.id.inputEditText).text?.toString()

            if (helper.isNotBlank()) {
                if (result.isNullOrBlank() || !CheeseCreator.isDateValid(result)) {
                    inputLayout.error = getString(R.string.invalid_date_error) +
                            " (" + getString(R.string.cheese_date_format) + ")"

                    return@setOnClickListener
                }
            }
            target.onDialogFinish(result)

            dialog?.dismiss()
        }

        return v
    }

    interface DialogListener {
        fun onDialogFinish(result: String?)
    }
}