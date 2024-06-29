package com.ofilip.exchange_rates.data.remote.dataStore


import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.remote.model.RatesTimeSeriesRemoteModel
import org.joda.time.DateTime

interface CurrencyRemoteDataStore {
    suspend fun getLatestRates(
        baseCurrencyCode: String,
        symbols: List<String>
    ): CurrencyRatesRemoteModel

    suspend fun getAllCurrencies(): List<CurrencyRemoteModel>

    suspend fun getRatesTimeSeries(
        startDate: DateTime,
        endDate: DateTime,
        base: String,
        symbols: List<String>?
    ): RatesTimeSeriesRemoteModel
}

