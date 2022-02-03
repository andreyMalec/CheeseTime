package com.malec.store

import kotlinx.coroutines.flow.flowOf

class TestBindActionSource2 : BindActionSource<TestAction>(
    requirement = { action -> action is TestAction.Action2 },
    source = {
        flowOf(TestAction.BindAction2("test bind data 2"))
    },
    error = { TestAction.BindAction2("test bind data 2") }
)