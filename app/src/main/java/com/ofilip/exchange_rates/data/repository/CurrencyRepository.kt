package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.core.entity.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    suspend fun prefetchCurrencies(): Result<Unit>

    fun getCurrencies(
        textQuery: String = "",
        onlyFavorites: Boolean = false
    ): Flow<Result<List<Currency>>>

    suspend fun updateCurrencyFavoriteState(
        currency: Currency,
        favoriteState: Boolean
    ): Result<Unit>

    fun getCurrency(currencyCode: String): Flow<Result<Currency?>>

    suspend fun areCurrenciesLoaded(): Result<Boolean>

    val overviewBaseCurrency: Flow<Result<String>>

    val conversionCurrencyFrom: Flow<Result<String>>

    val conversionCurrencyTo: Flow<Result<String>>

    suspend fun setOverviewBaseCurrency(currencyCode: String): Result<Unit>

    suspend fun setConversionCurrencyFrom(currencyCode: String): Result<Unit>

    suspend fun setConversionCurrencyTo(currencyCode: String): Result<Unit>
}


