package com.malec.cheesetime.ui.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.malec.cheesetime.R
import com.malec.cheesetime.model.Recipe
import com.malec.cheesetime.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingsActivity : BaseActivity(), RecipeAdapter.RecipeAction {
    private val viewModel: SettingsViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var adapter: RecipeAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveButton -> viewModel.saveSettings()

            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initToolbar()

        recipeAddButton.setOnClickListener {
            manageRecipe()
        }

        adapter = RecipeAdapter(this)
        recipeRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recipeRecycler.adapter = adapter

        viewModel.recipes.observe(this, Observer {
            adapter.submitList(it)
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

    private fun manageRecipe(recipe: Recipe? = null) {
        RecipeManageDialogFragment(recipe) { updatedRecipe ->
            viewModel.onSaveRecipe(updatedRecipe)
        }.show(supportFragmentManager, "")
    }

    override fun onClick(recipe: Recipe) {
        manageRecipe(recipe)
    }

    override fun onRemove(recipe: Recipe) {
        viewModel.onRemoveRecipe(recipe)
    }
}