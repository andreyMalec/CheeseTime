package com.malec.cheesetime.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivitySettingsBinding
import com.malec.cheesetime.model.Recipe
import com.malec.cheesetime.ui.base.BaseActivity

class SettingsActivity : BaseActivity(), RecipeAdapter.RecipeAction {
    private val viewModel: SettingsViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var adapter: RecipeAdapter

    private lateinit var binding: ActivitySettingsBinding

    override val navigator = AppNavigator(
        this,
        R.id.navHostFragment
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModel.saveSettings()
                showMessage(R.string.settings_save)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        binding.recipeAddButton.setOnClickListener {
            manageRecipe()
        }

        adapter = RecipeAdapter(this)
        binding.recipeRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recipeRecycler.adapter = adapter

        viewModel.recipes.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initToolbar() {
        binding.toolbar.setTitle(R.string.menu_settings)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarHeight =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
                    insets.getInsets(WindowInsets.Type.systemBars()).top
                else
                    insets.systemWindowInsetTop

            val h = (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
            val lp = binding.toolbar.layoutParams as ConstraintLayout.LayoutParams
            binding.toolbar.layoutParams = lp.also { it.height = h }
            binding.toolbar.updatePadding(top = statusBarHeight)
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