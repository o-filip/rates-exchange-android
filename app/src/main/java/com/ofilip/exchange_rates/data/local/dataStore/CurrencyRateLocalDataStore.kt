package com.ofilip.exchange_rates.data.local.dataStore

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRateLocalDataStore {
    fun getAll(): Flow<List<CurrencyRate>>

    suspend fun insert(currencyRates: List<CurrencyRate>)

    suspend fun deleteAll()

    val lastRatesLoadTimestampMs: Flow<Long>

    suspend fun setLastRatesLoadTimestampMs(timestamp: Long)
}

