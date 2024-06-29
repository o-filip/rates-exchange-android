package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

interface SetConversionCurrencyToUseCase {
    suspend fun execute(currency: String): Result<Unit>
}

class SetConversionCurrencyToUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : SetConversionCurrencyToUseCase {
    override suspend fun execute(currency: String): Result<Unit> =
        currencyRepository.setConversionCurrencyTo(currency)

}