package com.malec.store

import kotlinx.coroutines.flow.flowOf

class TestActionSource2 : ActionSource<TestAction>(
    source = {
        flowOf(TestAction.Action2("test data 2"))
    },
    error = {
        TestAction.Action2(it.toString())
    }
)