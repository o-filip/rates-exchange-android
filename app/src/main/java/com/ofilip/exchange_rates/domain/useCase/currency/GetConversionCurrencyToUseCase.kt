package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get the currency that the conversion is made from used in main conversion screen
 */
interface GetConversionCurrencyToUseCase {
    fun execute(): Flow<String>
}

class GetConversionCurrencyToUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : GetConversionCurrencyToUseCase {
    override fun execute(): Flow<String> = currencyRepository.conversionCurrencyTo
}
