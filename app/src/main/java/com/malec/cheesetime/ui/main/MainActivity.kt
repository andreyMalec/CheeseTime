package com.malec.cheesetime.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.updatePadding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.zxing.integration.android.IntentIntegrator
import com.malec.cheesetime.R
import com.malec.cheesetime.ui.BaseActivity
import com.malec.cheesetime.ui.allertDialogBuilder.LogoutDialog
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : BaseActivity(), HasAndroidInjector {
    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    override val navigator = SupportAppNavigator(
        this,
        supportFragmentManager,
        R.id.navHostFragment
    )

//    private var searchView: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//
//        searchView = menu?.findItem(R.id.appBarSearch)?.actionView as SearchView?
//        initSearchView()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scanButton -> viewModel.onScanClick(this)
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
        setContentView(R.layout.activity_main)

        initViewModelListeners()
        initTabHost()
        initToolbar()
        initClickListeners()

        mainNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settingsButton -> viewModel.showSettings()
            }

            true
        }
    }

    private fun initSearchView() {
//        searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
//            ?.setBackgroundResource(android.R.color.transparent)
//        searchView?.queryHint = getString(R.string.search_hint)
//
//        initSearchViewListener()
    }

    private fun initSearchViewListener() {
//        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?) = false
//
//            override fun onQueryTextChange(newText: String?): Boolean {
////                viewModel.searchQuery.value = newText
//
//                return false
//            }
//        })
    }

    private fun initViewModelListeners() {
        viewModel.showPressAgain.observe(this, { show ->
            if (show)
                showMessage(R.string.press_again_exit)
        })

        viewModel.userLogin.observe(this, {
            mainNavView.getHeaderView(0).findViewById<TextView>(R.id.userLoginText).text = it
        })
    }

    private fun initTabHost() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
        root.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarHeight = insets.systemWindowInsetTop

            val h = (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
            val lp = toolbar.layoutParams as AppBarLayout.LayoutParams
            toolbar.layoutParams = lp.also { it.height = h }
            toolbar.updatePadding(top = statusBarHeight)
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
        addFAB.setOnClickListener {
            viewModel.onFABClick()
        }

        logoutButton.setOnClickListener {
            LogoutDialog(this).setOnOkButtonClickListener {
                viewModel.logout()
            }.show()
        }
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}