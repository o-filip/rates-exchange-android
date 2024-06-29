package com.ofilip.exchange_rates.fixtures

import kotlinx.coroutines.flow.flowOf

fun <T> T.toFlowOfSuccess() = flowOf(Result.success(this))

fun <T> Exception.toFlowOfFailure() = flowOf(Result.failure<T>(this))
