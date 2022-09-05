package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.core.extensions.flatMapLatestResult
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface GetBaseRatesOfAllCurrenciesUseCase {
    fun execute(): Flow<Result<List<CurrencyRate>>>
}

class GetBaseRatesOfAllCurrenciesUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val currencyRateRepository: CurrencyRateRepository
) : GetBaseRatesOfAllCurrenciesUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<Result<List<CurrencyRate>>> =
        currencyRepository.getCurrencies().flatMapLatestResult { currencies ->
            val allCurrencyCodes = currencies.map { it.currencyCode }
            currencyRateRepository.getRates(allCurrencyCodes)
        }
}




