package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.malec.cheesetime.R

class AttachSourceDialog(private val onDialogFinish: (result: DialogResult) -> Unit) :
    DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_attach, container, false)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        v.findViewById<ListView>(R.id.sourceList).setOnItemClickListener { _, _, position, _ ->
            val result = when (position) {
                0 -> DialogResult.GALLERY
                else -> DialogResult.CAMERA
            }
            onDialogFinish(result)

            dialog?.dismiss()
        }

        return v
    }

    enum class DialogResult {
        GALLERY,
        CAMERA
    }
}