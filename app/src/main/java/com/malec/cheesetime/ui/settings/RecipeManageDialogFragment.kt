package com.malec.cheesetime.ui.settings

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Recipe
import kotlinx.android.synthetic.main.fragment_recipe_manage.*

class RecipeManageDialogFragment(
    private val recipe: Recipe? = null,
    private val onOkClick: (recipe: Recipe) -> Unit
) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_recipe_manage, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val nameEditText = v.findViewById<EditText>(R.id.nameEditText)
        val okButton = v.findViewById<TextView>(R.id.okButton)
        nameEditText.requestFocus()
        nameEditText.doAfterTextChanged {
            val color = if (it.isNullOrBlank()) {
                okButton.isEnabled = false
                ContextCompat.getColor(requireContext(), R.color.colorTextDisabled)
            } else {
                okButton.isEnabled = true
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            }
            okButton.setTextColor(color)
        }

        okButton.setOnClickListener {
            onOkClick(createRecipe())
            dialog?.dismiss()
        }

        v.findViewById<View>(R.id.cancelButton).setOnClickListener {
            dialog?.dismiss()
        }

        v.findViewById<View>(R.id.stageAddButton).setOnClickListener {
            addStage()
        }

        return v
    }

    private fun createRecipe(): Recipe {
        val stages = mutableSetOf<String>()
        for (c in stagesLayout.children) {
            val text = c.findViewById<EditText>(R.id.editText).text.toString().trim()
            val stage = text[0].toUpperCase() + text.drop(1)
            stages.add(stage)
        }
        val text = nameEditText.text.toString().trim()
        val name = text[0].toUpperCase() + text.drop(1)
        return Recipe(recipe?.id ?: 0, name, stages.joinToString("♂"))
    }

    private fun addStage(text: String? = null) {
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newStageLayout = inflater.inflate(R.layout.item_removable_edittext, null)
        stagesLayout.addView(newStageLayout, stagesLayout.childCount)
        val editText = newStageLayout.findViewById<EditText>(R.id.editText)
        editText.requestFocus()
        newStageLayout.findViewById<View>(R.id.removeButton).setOnClickListener {
            stagesLayout.removeView(it.parent as View)
        }
        text?.let {
            editText.setText(text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        if (recipe != null) {
            nameEditText.setText(recipe.name)
            for (stage in recipe.stages.split("♂"))
                addStage(stage)
        }
    }
}