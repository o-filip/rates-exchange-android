package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get the currency that the conversion is made from used used in main conversion screen
 */
interface GetConversionCurrencyFromUseCase {
    fun execute(): Flow<Result<String>>
}

class GetConversionCurrencyFromUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : GetConversionCurrencyFromUseCase {
    override fun execute(): Flow<Result<String>> = currencyRepository.conversionCurrencyFrom
}

