package com.malec.store

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class TestActionSource : ActionSource<TestAction>(
    source = {
        flow {
            var i = 0
            emit(TestAction.Action("TestActionSource, $i"))
            while (true) {
                i++
                kotlinx.coroutines.delay(1000)
                emit(TestAction.Action("TestActionSource, $i"))
            }
        }
    },
    error = {
        TestAction.Action(it.toString())
    }
)