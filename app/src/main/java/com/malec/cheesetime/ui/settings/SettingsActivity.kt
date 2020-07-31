package com.malec.cheesetime.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
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
        recipeLayout.addView(newRecipeLayout, recipeLayout.childCount)
        val editText = newRecipeLayout.findViewById<EditText>(R.id.editText)
        newRecipeLayout.findViewById<View>(R.id.removeButton).setOnClickListener {
            recipeLayout.removeView(it.parent as View)
        }
        text?.let {
            editText.setText(text)
        }
    }

    private fun clearRecipes() {
        recipeLayout.removeAllViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> saveSettings()

            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveSettings() {
        val recipes = mutableSetOf<String>()
        for (c in recipeLayout.children) {
            val text = c.findViewById<EditText>(R.id.editText).text.toString().trim()
            val recipe = text[0].toUpperCase() + text.drop(1)
            recipes.add(recipe)
        }
        viewModel.recipes.value = recipes.toList()

        viewModel.saveSettings()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initToolbar()

        recipeAddButton.setOnClickListener {
            addRecipe()
        }

        viewModel.recipes.observe(this, Observer {
            clearRecipes()
            it?.forEach { recipe ->
                if (recipe.isNotBlank())
                    addRecipe(recipe)
            }
        })
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.menu_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarHeight = insets.systemWindowInsetTop

            val h = (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
            val lp = toolbar.layoutParams as ConstraintLayout.LayoutParams
            toolbar.layoutParams = lp.also { it.height = h }
            toolbar.updatePadding(top = statusBarHeight)
            insets
        }
    }
}