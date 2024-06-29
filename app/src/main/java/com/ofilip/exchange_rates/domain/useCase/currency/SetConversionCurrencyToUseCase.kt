package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

/**
 * Use case to set and store the currency code to convert to used in main conversion screen
 */
interface SetConversionCurrencyToUseCase {
    suspend fun execute(currency: String): Result<Unit>
}

class SetConversionCurrencyToUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : SetConversionCurrencyToUseCase {
    override suspend fun execute(currency: String): Result<Unit> =
        currencyRepository.setConversionCurrencyTo(currency)

}
