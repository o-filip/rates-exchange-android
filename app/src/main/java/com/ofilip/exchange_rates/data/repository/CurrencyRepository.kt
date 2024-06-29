package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.core.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    suspend fun prefetchCurrencies()

    fun getCurrencies(
        textQuery: String = "",
        onlyFavorites: Boolean = false
    ): Flow<List<Currency>>

    suspend fun updateCurrencyFavoriteState(
        currency: Currency,
        favoriteState: Boolean
    )

    fun getCurrency(currencyCode: String): Flow<Currency?>

    suspend fun areCurrenciesLoaded(): Boolean

    val overviewBaseCurrency: Flow<String>

    val conversionCurrencyFrom: Flow<String>

    val conversionCurrencyTo: Flow<String>

    suspend fun setOverviewBaseCurrency(currencyCode: String)

    suspend fun setConversionCurrencyFrom(currencyCode: String)

    suspend fun setConversionCurrencyTo(currencyCode: String)
}


