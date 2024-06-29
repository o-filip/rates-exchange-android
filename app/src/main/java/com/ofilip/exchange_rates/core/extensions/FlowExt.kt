package com.ofilip.exchange_rates.core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch


/**
 * Launches coroutine in given [scope] and collects the flow and emits data into given [collector]
 */
fun <T> Flow<T>.collectIn(scope: CoroutineScope, collector: FlowCollector<T>): Job {
    return scope.launch { collect(collector) }
}

/**
 * Launches coroutine in given [scope] and emits [data] into flow
 */
fun <T> MutableSharedFlow<T>.emitIn(scope: CoroutineScope, data: T): Job {
    val flow = this
    return scope.launch {
        flow.emit(data)
    }
}

/**
 * Filters flow providing current and prev value in [predicate]
 */
fun <T> Flow<T>.filterWithPrev(predicate: suspend (prev: T?, current: T) -> Boolean): Flow<T> =
    flow {
        var prev: T? = null
        collect { value ->
            if (predicate(prev, value)) emit(value)
            prev = value
        }
    }

fun <T> Flow<T>.filterUnchanged(): Flow<T> = filterWithPrev { prev, current -> prev != current }

