package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

/**
 * Use case to set and store the currency code to convert from used in main conversion screen
 */
interface SetConversionCurrencyFromUseCase {
    suspend fun execute(currency: String): Result<Unit>
}

class SetConversionCurrencyFromUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : SetConversionCurrencyFromUseCase {
    override suspend fun execute(currency: String): Result<Unit> =
        currencyRepository.setConversionCurrencyFrom(currency)

}
