package com.malec.store

class TestSideEffect3 : SideEffect<TestState, TestAction>(
    requirement = { _, action -> action is TestAction.BindAction3 },
    effect = { _, _ ->
        TestAction.SideAction3("test side data 3")
    },
    error = { TestAction.SideAction3("test side data 3") }
)