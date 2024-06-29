package com.ofilip.exchange_rates.domain.useCase.rate

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Use case to get the rates of all currencies relative to the app base currency
 */
interface GetBaseRatesOfAllCurrenciesUseCase {
    fun execute(): Flow<List<CurrencyRate>>
}

class GetBaseRatesOfAllCurrenciesUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val currencyRateRepository: CurrencyRateRepository
) : GetBaseRatesOfAllCurrenciesUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<List<CurrencyRate>> =
        currencyRepository.getCurrencies().flatMapLatest { currencies ->
            val allCurrencyCodes = currencies.map { it.currencyCode }
            currencyRateRepository.getRates(allCurrencyCodes)
        }
}




