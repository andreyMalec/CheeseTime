package com.malec.cheesetime.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.malec.cheesetime.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navHolder: NavigatorHolder

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    private val navigator = TabNavigator(
        this,
        supportFragmentManager,
        R.id.navHostFragment
    )

    private var searchView: SearchView? = null

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        searchView = menu?.findItem(R.id.appBarSearch)?.actionView as SearchView?
        initSearchView()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scanButton -> viewModel.onScanClick()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModelListeners()
        initTabHost()
        initToolbar()
    }

    private fun initViewModelListeners() {
        viewModel.showPressAgain.observe(this, Observer { show ->
            if (show)
                Toast.makeText(this, getString(R.string.pressAgainExit), Toast.LENGTH_SHORT).show()
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
    }

    private fun initSearchView() {
        searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
            ?.setBackgroundResource(android.R.color.transparent)
        searchView?.queryHint = getString(R.string.search_hint)

        initSearchViewListener()
    }

    private fun initSearchViewListener() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
//                viewModel.searchQuery.value = newText

                return false
            }
        })
    }

    override fun onPause() {
        super.onPause()
        navHolder.removeNavigator()
    }

    override fun onResume() {
        super.onResume()
        navHolder.setNavigator(navigator)
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}