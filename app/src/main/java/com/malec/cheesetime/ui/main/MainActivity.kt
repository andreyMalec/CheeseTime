package com.malec.cheesetime.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowInsets
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.updatePadding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.zxing.integration.android.IntentIntegrator
import com.malec.cheesetime.R
import com.malec.cheesetime.databinding.ActivityMainBinding
import com.malec.cheesetime.ui.allertDialogBuilder.LogoutDialog
import com.malec.cheesetime.ui.base.BaseActivity
import dagger.android.HasAndroidInjector

class MainActivity : BaseActivity(), HasAndroidInjector {
    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    override val navigator = ResultNavigator(
        this,
        R.id.navHostFragment
    )

    private lateinit var binding: ActivityMainBinding

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scanButton -> viewModel.onScanClick()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(resultCode, data)

        result.contents?.let {
            viewModel.onScanSuccess(it)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        initViewModelListeners()
        initTabHost()
        initToolbar()
        initClickListeners()

        binding.mainNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settingsButton -> viewModel.showSettings()
            }

            true
        }
    }

    private fun initViewModelListeners() {
        viewModel.showPressAgain.observe(this, { show ->
            if (show)
                showMessage(R.string.press_again_exit)
        })

        viewModel.userLogin.observe(this, {
            binding.mainNavView.getHeaderView(0).findViewById<TextView>(R.id.userLoginText).text =
                it
        })
    }

    private fun initTabHost() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.onTaskListClick()
                    1 -> viewModel.onCheeseListClick()
                    2 -> viewModel.onReportsClick()
                }
            }
        })
    }

    private fun initToolbar() {
        binding.toolbar.setTitle(R.string.app_name)
        setSupportActionBar(binding.toolbar)
        binding.root.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarHeight =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
                    insets.getInsets(WindowInsets.Type.systemBars()).top
                else
                    insets.systemWindowInsetTop

            val h = (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
            val lp = binding.toolbar.layoutParams as AppBarLayout.LayoutParams
            binding.toolbar.layoutParams = lp.also { it.height = h }
            binding.toolbar.updatePadding(top = statusBarHeight)
            insets
        }

        //TODO починить иконку меню при выборе сыров
//        val drawerToggle = object : ActionBarDrawerToggle(
//            this,
//            mainDrawer,
//            toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        ) {
//
//        }
//        mainDrawer.addDrawerListener(drawerToggle)
//        drawerToggle.syncState()
    }

    private fun initClickListeners() {
        binding.addFAB.setOnClickListener {
            viewModel.onFABClick()
        }

        binding.logoutButton.setOnClickListener {
            LogoutDialog(this).setOnOkButtonClickListener {
                viewModel.logout()
            }.show()
        }
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}