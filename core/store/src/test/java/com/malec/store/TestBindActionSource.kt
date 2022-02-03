package com.malec.store

import kotlinx.coroutines.flow.flowOf

class TestBindActionSource : BindActionSource<TestAction>(
    requirement = { action -> action is TestAction.Action },
    source = {
        flowOf(TestAction.BindAction("test bind data"))
    },
    error = { TestAction.BindAction(it.toString()) }
)