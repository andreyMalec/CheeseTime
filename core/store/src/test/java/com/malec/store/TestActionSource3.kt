package com.malec.store

import kotlinx.coroutines.flow.flowOf

class TestActionSource3 : ActionSource<TestAction>(
    source = {
        flowOf(TestAction.Action3("test data 3"))
    },
    error = {
        TestAction.Action3(it.toString())
    }
)