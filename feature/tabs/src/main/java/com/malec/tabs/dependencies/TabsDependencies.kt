package com.malec.tabs.dependencies

import com.malec.domain.repository.UserRepo
import com.malec.presentation.unidirectional.BaseDependencies

interface TabsDependencies : BaseDependencies {
    fun getUserRepo(): UserRepo
    fun getTabsOutput(): TabsOutput
}