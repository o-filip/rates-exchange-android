package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

/**
 * Use case to initialize the app
 */
interface InitializeAppUseCase {
    suspend fun execute(): Result<Unit>
}

class InitializeAppUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : InitializeAppUseCase {
    override suspend fun execute(): Result<Unit> =
        try {
            if (!currencyRepository.areCurrenciesLoaded().getOrThrow()) {
                currencyRepository.prefetchCurrencies()
            } else {
                Result.success(Unit)
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
}

