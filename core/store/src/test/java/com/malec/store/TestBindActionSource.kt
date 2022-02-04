package com.malec.store

import kotlinx.coroutines.flow.flowOf

class TestBindActionSource : BindActionSource<TestAction>(
    requirement = { action -> action is TestAction.Action },
    source = {
        flowOf(TestAction.BindAction("TestBindActionSource"))
    },
    error = { TestAction.BindAction("TestBindActionSource") }
)