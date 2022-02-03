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
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.FragmentRecipeManageBinding
import com.malec.domain.model.Recipe

class RecipeManageDialogFragment(
    private val recipe: Recipe? = null,
    private val onOkClick: (recipe: Recipe) -> Unit
) : DialogFragment() {

    private var _binding: FragmentRecipeManageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeManageBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val nameEditText = binding.nameEditText
        val okButton = binding.okButton
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

        binding.cancelButton.setOnClickListener {
            dialog?.dismiss()
        }

        binding.stageAddButton.setOnClickListener {
            addStage()
        }

        return binding.root
    }

    private fun createRecipe(): Recipe {
        val stages = mutableSetOf<String>()
        for (c in binding.stagesLayout.children) {
            val text = c.findViewById<EditText>(R.id.editText).text.toString().trim()
            val stage = text[0].toUpperCase() + text.drop(1)
            stages.add(stage)
        }
        val text = binding.nameEditText.text.toString().trim()
        val name = text[0].uppercaseChar() + text.drop(1)
        return Recipe(recipe?.id ?: 0, name, stages.toList())
    }

    private fun addStage(text: String? = null) {
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newStageLayout = inflater.inflate(R.layout.item_removable_edittext, null)
        binding.stagesLayout.addView(newStageLayout, binding.stagesLayout.childCount)
        val editText = newStageLayout.findViewById<EditText>(R.id.editText)
        editText.requestFocus()
        newStageLayout.findViewById<View>(R.id.removeButton).setOnClickListener {
            binding.stagesLayout.removeView(it.parent as View)
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
            binding.nameEditText.setText(recipe.name)
            for (stage in recipe.stages)
                addStage(stage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}