package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get currencies filtered by a text query applied to the currency code or name
 * or by the favorite status
 */
interface GetFilteredCurrenciesUseCase {
    suspend fun execute(query: String, onlyFavorites: Boolean): Flow<List<Currency>>
}

class GetFilteredCurrenciesUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : GetFilteredCurrenciesUseCase {
    override suspend fun execute(
        query: String,
        onlyFavorites: Boolean
    ): Flow<List<Currency>> =
        currencyRepository.getCurrencies(textQuery = query, onlyFavorites = onlyFavorites)
}
