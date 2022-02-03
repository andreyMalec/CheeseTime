package com.malec.store

class TestReducer : Reducer<TestState, TestAction> {
    override fun reduce(state: TestState, action: TestAction): TestState {
        return when (action) {
            is TestAction.NoAction -> state.copy(
                value = "no action"
            )
            is TestAction.Action -> state.copy(
                value = action.value
            )
            is TestAction.Action2 -> state.copy(
                value = action.value
            )
            is TestAction.Action3 -> state.copy(
                value = action.value
            )
            is TestAction.BindAction -> state.copy(
                value = action.value
            )
            is TestAction.BindAction2 -> state.copy(
                value = action.value
            )
            is TestAction.BindAction3 -> state.copy(
                value = action.value
            )
            is TestAction.SideAction -> state.copy(
                value = action.value
            )
            is TestAction.SideAction2 -> state.copy(
                value = action.value
            )
            is TestAction.SideAction3 -> state.copy(
                value = action.value
            )

            else -> state.copy()
        }
    }
}