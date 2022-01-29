package com.github.philippheuer.events4j.kotlin

import com.github.philippheuer.events4j.api.IEventManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Creates a flow for specific events.
 *
 * Automatically disposes of the subscription when there are no more collectors.
 *
 * @return  A Flow object that encapsulates the eventListener and handles the subscription
 */
inline fun <reified T> IEventManager.flowOn(): Flow<T> = flowOn(T::class.java)

/**
 * Creates a flow for specific events.
 *
 * Automatically disposes of the subscription when there are no more collectors.
 *
 * @param klass The class to receive events for
 * @return      A Flow object that encapsulates the eventListener and handles the subscription
 */
fun <T> IEventManager.flowOn(
    klass: Class<T>
): Flow<T> = callbackFlow {
    val subscription = onEvent(klass, ::trySend)

    awaitClose {
        subscription.dispose()
    }
}
