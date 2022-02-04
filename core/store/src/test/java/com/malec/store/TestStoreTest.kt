package com.malec.store

import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CopyOnWriteArrayList

class TestStoreTest {
    private val errorHandler = object : ErrorHandler {
        override fun handle(t: Throwable) {

        }
    }

    private fun store() = TestStore(
        TestState("init"),
        TestReducer(),
        errorHandler,
        actionSources = CopyOnWriteArrayList(
            listOf(
                TestActionSource(),
                TestActionSource2(),
                TestActionSource3()
            )
        ),
        bindActionSources = CopyOnWriteArrayList(
            listOf(
                TestBindActionSource(),
                TestBindActionSource2(),
                TestBindActionSource3()
            )
        ),
        sideEffects = CopyOnWriteArrayList(
            listOf(
                TestSideEffect(),
                TestSideEffect2(),
                TestSideEffect3()
            )
        )
    )

    private var actions = CopyOnWriteArrayList<String>()

    @Test
    fun test() = runBlocking {
        val store = store()

        val job = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            store.state.collect { state ->
                actions.add(state.value)
            }
        }
        delay(100)
        store.dispatchAction(TestAction.NoAction)
        delay(100)
        Assert.assertEquals(
            listOf(
                "init",
                "test data",
                "test data 2",
                "test data 3",
                "test bind data",
                "test bind data 2",
                "test bind data 3",
                "test side data",
                "test side data 2",
                "test side data 3",
                "no action"
            ).sorted(), actions.sorted()
        )
        job.cancel()
        actions.clear()
    }

    @Test
    fun test2() = runBlocking {
        val store = store()

        val job = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            store.state.collect { state ->
                println(state.value)
                actions.add(state.value)
            }
        }
        delay(3000)
        store.dispatchAction(TestAction.NoAction)
        delay(60000)
        job.cancel()
        actions.clear()
    }

    @Test
    fun `test x50`() = runBlocking {
        repeat(50) {
            test()
            delay(100)
        }
    }
}