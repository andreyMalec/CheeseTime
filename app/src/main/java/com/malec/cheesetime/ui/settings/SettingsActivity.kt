package com.malec.cheesetime.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import com.malec.cheesetime.R
import com.malec.cheesetime.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingsActivity : BaseActivity() {
    private val viewModel: SettingsViewModel by viewModels {
        viewModelFactory
    }

    private fun addRecipe(text: String? = null) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newRecipeLayout = inflater.inflate(R.layout.item_removable_text, null)
        recipeLayout.addView(newRecipeLayout, recipeLayout.childCount - 1)
        val editText = newRecipeLayout.findViewById<EditText>(R.id.editText)
        newRecipeLayout.findViewById<View>(R.id.removeButton).setOnClickListener {
            recipeLayout.removeView(it.parent as View)
        }
        text?.let {
            editText.setText(text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


    }
}