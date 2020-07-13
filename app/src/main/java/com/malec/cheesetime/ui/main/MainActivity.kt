package com.malec.cheesetime.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModelListeners()
        initTabHost()
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
                    2 -> viewModel.onReportClick()
                }
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