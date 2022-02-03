package com.malec.main.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import com.malec.injection.ComponentOwner
import com.malec.main.R
import com.malec.main.dependencies.MainActivityResultContracts
import com.malec.main.di.MainComponent
import com.malec.main.di.MainComponentProvider
import com.malec.main.presentation.store.MainState
import com.malec.main.presentation.viewcontroller.MainViewController
import com.malec.presentation.base.BaseActivity
import com.malec.presentation.databinding.ActivityFragmentContainerBinding
import com.malec.presentation.navigation.BackButtonListener
import com.malec.presentation.navigation.ResultNavigator
import com.malec.presentation.navigation.ScreenContainers
import com.malec.presentation.unidirectional.BaseView
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityFragmentContainerBinding>(),
    ComponentOwner<MainComponent>, BaseView<MainState> {

    @Inject
    internal lateinit var viewController: MainViewController

    @Inject
    internal lateinit var resultContracts: MainActivityResultContracts

    @Inject
    internal lateinit var screenContainers: ScreenContainers

    override var navigator = ResultNavigator(
        this,
        R.id.fragmentContainer,
        mapOf()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewController)
        setNavigator()
    }

    private fun setNavigator() {
        val launchers = resultContracts.contracts.mapValues {
            registerForActivityResult(it.value, this)
        }
        navigator = ResultNavigator(this, R.id.fragmentContainer, launchers)
        navigator.setContainers(screenContainers.containers)
        navHolder.setNavigator(navigator)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment is BackButtonListener)
            fragment.onBackPressed()
        else
            finish()
    }

    override fun createViewBinding(inflater: LayoutInflater) =
        ActivityFragmentContainerBinding.inflate(layoutInflater)

    override fun provideComponent() = MainComponentProvider.newInstance().component

    override fun inject(component: MainComponent) {
        component.inject(this)
    }

    override fun renderState(state: MainState) {

    }
}