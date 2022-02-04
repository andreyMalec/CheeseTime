package com.malec.store

class TestSideEffect : SideEffect<TestState, TestAction>(
    requirement = { _, action -> action is TestAction.BindAction },
    effect = { _, _ ->
        TestAction.SideAction("TestSideEffect")
    },
    error = { TestAction.SideAction("TestSideEffect") }
)