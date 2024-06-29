package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

/**
 * Use case to set and store the base currency for the overview
 */
interface SetOverviewBaseCurrencyUseCase {
    suspend fun execute(currencyCode: String)
}

class SetOverviewBaseCurrencyUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : SetOverviewBaseCurrencyUseCase {
    override suspend fun execute(currencyCode: String) =
        currencyRepository.setOverviewBaseCurrency(currencyCode)

}
