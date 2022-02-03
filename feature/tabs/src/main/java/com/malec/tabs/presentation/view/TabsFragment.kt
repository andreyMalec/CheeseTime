package com.malec.tabs.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.tabs.TabLayout
import com.malec.injection.ComponentOwner
import com.malec.presentation.base.BaseFragment
import com.malec.presentation.initToolbar
import com.malec.presentation.isDisplayHomeAsUpEnabled
import com.malec.presentation.navigation.BackButtonListener
import com.malec.presentation.unidirectional.BaseView
import com.malec.tabs.R
import com.malec.tabs.databinding.FragmentTabsBinding
import com.malec.tabs.di.TabsComponent
import com.malec.tabs.di.TabsComponentProvider
import com.malec.tabs.presentation.store.TabsState
import com.malec.tabs.presentation.viewcontroller.TabsViewController
import javax.inject.Inject

class TabsFragment : BaseFragment<FragmentTabsBinding>(), BaseView<TabsState>,
    ComponentOwner<TabsComponent>, BackButtonListener {
    @Inject
    internal lateinit var viewController: TabsViewController

    private var actionBar: ActionBar? = null

    private val tabListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            when (tab?.position) {
                0 -> viewController.onClickTasks()
                1 -> viewController.onClickCheeses()
                2 -> viewController.onClickReports()
            }
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {}
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentTabsBinding.inflate(inflater, parent, attachToParent)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initTabHost()
        binding.mainNavView.findViewById<LinearLayout>(R.id.logoutButton).setOnClickListener {
            LogoutDialog(requireContext()).setOnOkButtonClickListener {
                viewController.onCLickLogout()
            }.show()
        }
    }

    private fun initTabHost() {
        binding.tabLayout.addOnTabSelectedListener(tabListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewController)
    }

    private fun initToolbar() {
        val activity = (requireActivity() as AppCompatActivity)
        activity.initToolbar(binding.root, binding.toolbar, R.string.app_name)
        actionBar = activity.supportActionBar

        val drawerToggle = object : ActionBarDrawerToggle(
            requireActivity(),
            binding.mainDrawer,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {}
        binding.mainDrawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        binding.toolbar.setNavigationOnClickListener {
            if (!actionBar.isDisplayHomeAsUpEnabled())
                binding.mainDrawer.openDrawer(GravityCompat.START)
            else
                onBackPressed()
        }
    }

    override fun onBackPressed() {
        val fragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.tabsFragmentContainer)
        if (fragment is BackButtonListener)
            fragment.onBackPressed()
        else
            viewController.onClickExit()
    }

    override fun provideComponent() = TabsComponentProvider.getInstance().component

    override fun inject(component: TabsComponent) {
        component.inject(this)
    }

    override fun renderState(state: TabsState) {
        if (binding.tabLayout.selectedTabPosition != state.currentTab.position) {
            binding.tabLayout.removeOnTabSelectedListener(tabListener)
            binding.tabLayout.getTabAt(state.currentTab.position)?.select()
            initTabHost()
        }
    }

    companion object {
        fun newInstance() = TabsFragment()
    }
}