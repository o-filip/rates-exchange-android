package com.ofilip.exchange_rates.data.remote.ktor.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper<T>(
    val response: T,
)
