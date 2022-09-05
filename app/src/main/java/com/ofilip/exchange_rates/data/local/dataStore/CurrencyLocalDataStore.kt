package com.ofilip.exchange_rates.data.local.dataStore

import com.ofilip.exchange_rates.core.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyLocalDataStore {
    fun getAll(
        textQuery: String = "",
        onlyFavorites: Boolean = false
    ): Flow<List<Currency>>

    fun getByCode(currencyCode: String): Flow<Currency>

    suspend fun insert(currencies: List<Currency>)

    suspend fun update(currency: Currency)

    suspend fun delete(currencies: List<Currency>)

    val overviewBaseCurrency: Flow<String>

    val conversionCurrencyFrom: Flow<String>

    val conversionCurrencyTo: Flow<String>

    suspend fun setOverviewBaseCurrency(currencyCode: String)

    suspend fun setConversionCurrencyFrom(currencyCode: String)

    suspend fun setConversionCurrencyTo(currencyCode: String)
}


