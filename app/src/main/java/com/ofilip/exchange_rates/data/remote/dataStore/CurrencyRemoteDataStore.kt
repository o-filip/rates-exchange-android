package com.ofilip.exchange_rates.data.remote.dataStore


import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel

interface CurrencyRemoteDataStore {
    suspend fun getLatestRates(
        baseCurrencyCode: String,
        symbols: List<String>
    ): CurrencyRatesRemoteModel

    suspend fun getAllCurrencies(): List<CurrencyRemoteModel>
}

