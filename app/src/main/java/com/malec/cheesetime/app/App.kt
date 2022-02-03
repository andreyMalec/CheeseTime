package com.malec.cheesetime.app

import android.app.Application
import com.malec.cheesetime.di.AppComponent
import com.malec.cheesetime.di.DaggerAppComponent
import com.malec.cheesetime.di.ProvideComponents
import com.malec.cheesetime.di.module.ContextModule
import com.malec.injection.ComponentOwner
import com.malec.injection.InjectionHolder

class App : Application(), ComponentOwner<AppComponent> {

    private val componentOwnerLifecycle =
        InjectionHolder.instance.getComponentOwnerLifeCycle(this)

    override fun onCreate() {
        super.onCreate()
        componentOwnerLifecycle.onCreate()
        InjectionHolder.init(this)
    }

    override fun provideComponent(): AppComponent {
        val appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()

        ProvideComponents.provide(AppComponent::class.java)

        return appComponent
    }

    override fun inject(component: AppComponent) {
        component.inject(this)
    }
}