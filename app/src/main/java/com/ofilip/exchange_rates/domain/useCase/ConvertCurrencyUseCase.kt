package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.core.error.DomainError
import javax.inject.Inject
import kotlinx.coroutines.flow.first

interface ConvertCurrencyUseCase {
    suspend fun execute(
        amount: Double,
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): Result<Double?>
}

class ConvertCurrencyUseCaseImpl @Inject constructor(
    private val getRatesOfAllCurrenciesUseCase: GetBaseRatesOfAllCurrenciesUseCase,
    private val applyConversionRateToAmountUseCase: ApplyConversionRateToAmountUseCase
) : ConvertCurrencyUseCase {
    override suspend fun execute(
        amount: Double,
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): Result<Double?> =
        getRatesOfAllCurrenciesUseCase.execute().first().map { rates ->
            val fromCurrencyRate = rates.find { currency -> currency.currency == fromCurrencyCode }
            val toCurrencyRate = rates.find { currency -> currency.currency == toCurrencyCode }

            if (fromCurrencyRate == null) {
                return Result.failure(DomainError.CurrencyRateNotFound(fromCurrencyCode))
            } else if (toCurrencyRate == null) {
                return Result.failure(DomainError.CurrencyRateNotFound(toCurrencyCode))
            }

            applyConversionRateToAmountUseCase.execute(
                amount,
                fromCurrencyRate.rate,
                toCurrencyRate.rate
            )
        }
}
