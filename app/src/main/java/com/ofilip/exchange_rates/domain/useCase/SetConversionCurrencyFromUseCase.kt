package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

interface SetConversionCurrencyFromUseCase {
    suspend fun execute(currency: String): Result<Unit>
}

class SetConversionCurrencyFromUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : SetConversionCurrencyFromUseCase {
    override suspend fun execute(currency: String): Result<Unit> =
        currencyRepository.setConversionCurrencyFrom(currency)

}