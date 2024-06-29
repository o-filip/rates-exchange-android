package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

/**
 * Use case to initialize the app
 */
interface InitializeAppUseCase {
    suspend fun execute()
}

class InitializeAppUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : InitializeAppUseCase {
    override suspend fun execute() {
        if (!currencyRepository.areCurrenciesLoaded()) {
            currencyRepository.prefetchCurrencies()
        }
    }
}

