package com.malec.cheesetime.di

import com.malec.cheesedetail.di.CheeseDetailProvideComponent
import com.malec.cheeselist.di.CheeseListProvideComponent
import com.malec.main.di.MainProviderComponent
import com.malec.signin.di.SignInProvideComponent
import com.malec.tabs.di.TabsProvideComponent
import com.malec.taskdetail.di.TaskDetailProvideComponent
import com.malec.tasklist.di.TaskListProvideComponent

object ProvideComponents {
    fun provide(appComponentClass: Class<AppComponent>) {
        SignInProvideComponent.provide(appComponentClass)
        MainProviderComponent.provide(appComponentClass)
        TabsProvideComponent.provide(appComponentClass)
        CheeseListProvideComponent.provide(appComponentClass)
        TaskListProvideComponent.provide(appComponentClass)
        TaskDetailProvideComponent.provide(appComponentClass)
        CheeseDetailProvideComponent.provide(appComponentClass)
    }
}