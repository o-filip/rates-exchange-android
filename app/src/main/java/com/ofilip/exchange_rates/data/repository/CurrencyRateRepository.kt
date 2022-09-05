package com.ofilip.exchange_rates.data.repository


import com.ofilip.exchange_rates.core.entity.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRateRepository {
    val lastCurrencyRateLoadTimestampMs: Flow<Result<Long>>

    fun getRates(currencyCodes: List<String>): Flow<Result<List<CurrencyRate>>>
}

