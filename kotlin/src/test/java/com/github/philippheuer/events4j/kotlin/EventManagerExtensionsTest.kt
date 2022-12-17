package com.github.philippheuer.events4j.kotlin

import com.github.philippheuer.events4j.core.EventManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class EventManagerExtensionsTest {

    @Test
    fun `Test if we only receive the events we asked for`() = runTest {
        val eventManager = EventManager()
        eventManager.autoDiscovery()

        var eventsReceived = 0
        val collectorJob = launch {
            eventManager.flowOn<TestEvent1>()
                .collect {
                    assertEquals(TestEvent1, it, "Received something else than testEvent1")
                    eventsReceived++
                }
        }
        runCurrent()

        launch {
            eventManager.apply {
                publish(TestEvent1)
                publish(TestEvent1)
                publish(TestEvent2)
                publish(TestEvent1)
                publish(TestEvent2)
                publish(TestEvent1)
            }
        }
        runCurrent()

        collectorJob.cancel()

        assertEquals(4, eventsReceived, "Expected the amount of events received to be exactly 4")
    }

    @Test
    fun `Test if the subscription is correctly cleaned up`() = runTest {
        val eventManager = EventManager()
        eventManager.autoDiscovery()

        val collectJob = launch {
            eventManager.flowOn<TestEvent1>()
                .collect { /* NO-OP */ }
        }

        runCurrent()
        assertEquals(1, eventManager.activeSubscriptions.size, "Subscription isn't added")

        collectJob.cancel()
        runCurrent()
        assertEquals(0, eventManager.activeSubscriptions.size, "The subscription was not correctly cleaned up")
    }

    object TestEvent1
    object TestEvent2
}
