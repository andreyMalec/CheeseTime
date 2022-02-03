package com.malec.tabs.presentation.store

import com.malec.tabs.presentation.Tab

data class TabsState(
    val currentTab: Tab = Tab.None
)