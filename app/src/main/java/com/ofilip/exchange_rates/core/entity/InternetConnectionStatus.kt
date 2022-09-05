package com.ofilip.exchange_rates.core.entity

data class InternetConnectionStatus(
    val isConnected: Boolean,
    val lastDataLoadedTimestampMs: Long?
)
