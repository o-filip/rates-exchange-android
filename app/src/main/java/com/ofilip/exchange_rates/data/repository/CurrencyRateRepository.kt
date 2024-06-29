package com.ofilip.exchange_rates.data.repository


import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.core.entity.RatesTimeSeriesItem
import java.util.Date
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

interface CurrencyRateRepository {
    val lastCurrencyRateLoadTimestampMs: Flow<Long>

    fun getRates(currencyCodes: List<String>): Flow<List<CurrencyRate>>

    suspend fun getRatesTimeSeries(
        startDate: DateTime,
        endDate: DateTime,
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): List<RatesTimeSeriesItem>
}

