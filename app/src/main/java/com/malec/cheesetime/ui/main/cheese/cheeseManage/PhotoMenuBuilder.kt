package com.malec.cheesetime.ui.main.cheese.cheeseManage

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.malec.cheesetime.R

class PhotoMenuBuilder {
    private var onDownloadClick: (() -> Unit)? = null
    private var onShareClick: (() -> Unit)? = null
    private var onDeleteClick: (() -> Unit)? = null

    fun show(fragmentManager: FragmentManager) {
        BottomSheetFragment(onDownloadClick, onShareClick, onDeleteClick).show(fragmentManager, "")
    }

    fun setOnDownloadClickListener(listener: () -> Unit): PhotoMenuBuilder {
        onDownloadClick = listener
        return this
    }

    fun setOnShareClickListener(listener: () -> Unit): PhotoMenuBuilder {
        onShareClick = listener
        return this
    }

    fun setOnDeleteClickListener(listener: () -> Unit): PhotoMenuBuilder {
        onDeleteClick = listener
        return this
    }

    class BottomSheetFragment(
        private var onDownloadClick: (() -> Unit)? = null,
        private var onShareClick: (() -> Unit)? = null,
        private var onDeleteClick: (() -> Unit)? = null
    ) : BottomSheetDialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.layout_photo_menu, container, false)

            v.findViewById<LinearLayout>(R.id.downloadButton).setOnClickListener {
                onDownloadClick?.invoke()
                Handler().postDelayed({
                    dialog?.dismiss()
                }, 200)
            }
            v.findViewById<LinearLayout>(R.id.shareButton).setOnClickListener {
                onShareClick?.invoke()
                Handler().postDelayed({
                    dialog?.dismiss()
                }, 200)
            }
            v.findViewById<LinearLayout>(R.id.deleteButton).setOnClickListener {
                onDeleteClick?.invoke()
                Handler().postDelayed({
                    dialog?.dismiss()
                }, 200)
            }

            return v
        }
    }
}