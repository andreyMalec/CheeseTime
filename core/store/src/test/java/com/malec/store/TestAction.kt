package com.malec.store

sealed class TestAction {
    object NoAction : TestAction() {
        override fun toString(): String {
            return "NoAction"
        }
    }

    data class Action(val value: String) : TestAction()

    data class Action2(val value: String) : TestAction()

    data class Action3(val value: String) : TestAction()

    data class BindAction(val value: String) : TestAction()

    data class BindAction2(val value: String) : TestAction()

    data class BindAction3(val value: String) : TestAction()

    data class SideAction(val value: String) : TestAction()

    data class SideAction2(val value: String) : TestAction()

    data class SideAction3(val value: String) : TestAction()
}