package com.malec.tabs.presentation.store.sideeffect

import com.malec.domain.repository.UserRepo
import com.malec.store.SideEffect
import com.malec.tabs.dependencies.TabsOutput
import com.malec.tabs.presentation.store.TabsAction
import com.malec.tabs.presentation.store.TabsState

class LogoutSideEffect(
    private val userRepo: UserRepo,
    private val tabsOutput: TabsOutput
) : SideEffect<TabsState, TabsAction>(
    requirement = { _, action -> action is TabsAction.Logout },
    effect = { _, _ ->
        userRepo.logout()
        tabsOutput.logout()
        TabsAction.Ignore
    },
    error = {
        TabsAction.Error(it)
    }
)