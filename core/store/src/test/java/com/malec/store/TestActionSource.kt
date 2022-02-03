package com.malec.store

import kotlinx.coroutines.flow.flowOf

class TestActionSource : ActionSource<TestAction>(
    source = {
        flowOf(TestAction.Action("test data"))
    },
    error = {
        TestAction.Action(it.toString())
    }
)