package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

interface SetOverviewBaseCurrencyUseCase {
    suspend fun execute(currencyCode: String)
}

class SetOverviewBaseCurrencyUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : SetOverviewBaseCurrencyUseCase {
    override suspend fun execute(currencyCode: String) {
        currencyRepository.setOverviewBaseCurrency(currencyCode)
    }
}