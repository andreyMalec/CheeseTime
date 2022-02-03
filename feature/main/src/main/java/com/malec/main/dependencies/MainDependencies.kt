package com.malec.main.dependencies

import com.github.terrakok.cicerone.NavigatorHolder
import com.malec.domain.repository.UserRepo
import com.malec.presentation.navigation.ScreenContainers
import com.malec.presentation.unidirectional.BaseDependencies

interface MainDependencies : BaseDependencies {
    fun getUserRepo(): UserRepo
    fun getNavHolder(): NavigatorHolder
    fun mainOutput(): MainOutput
    fun mainActivityResultContracts(): MainActivityResultContracts
    fun getScreenContainers(): ScreenContainers
}