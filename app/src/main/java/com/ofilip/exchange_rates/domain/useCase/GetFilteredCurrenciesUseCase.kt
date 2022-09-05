package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface GetFilteredCurrenciesUseCase {
    suspend fun execute(query: String, onlyFavorites: Boolean): Flow<Result<List<Currency>>>
}

class GetFilteredCurrenciesUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : GetFilteredCurrenciesUseCase {
    override suspend fun execute(
        query: String,
        onlyFavorites: Boolean
    ): Flow<Result<List<Currency>>> =
        currencyRepository.getCurrencies(textQuery = query, onlyFavorites = onlyFavorites)
}
