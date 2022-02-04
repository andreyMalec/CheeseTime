package com.malec.store

import kotlinx.coroutines.flow.flowOf

class TestBindActionSource3 : BindActionSource<TestAction>(
    requirement = { action -> action is TestAction.Action3 },
    source = {
        flowOf(TestAction.BindAction3("TestBindActionSource3"))
    },
    error = { TestAction.BindAction3("TestBindActionSource3") }
)