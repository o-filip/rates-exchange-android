package com.ofilip.exchange_rates.domain.useCase.conversion

import com.ofilip.exchange_rates.core.error.DomainError
import com.ofilip.exchange_rates.domain.useCase.rate.GetBaseRatesOfAllCurrenciesUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.first

interface ConvertCurrencyUseCase {
    suspend fun execute(
        amount: Double,
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): Double?
}

class ConvertCurrencyUseCaseImpl @Inject constructor(
    private val getRatesOfAllCurrenciesUseCase: GetBaseRatesOfAllCurrenciesUseCase,
    private val applyConversionRateToAmountUseCase: ApplyConversionRateToAmountUseCase
) : ConvertCurrencyUseCase {
    override suspend fun execute(
        amount: Double,
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): Double? =
        getRatesOfAllCurrenciesUseCase.execute().first().let { rates ->
            val fromCurrencyRate = rates.find { currency -> currency.currency == fromCurrencyCode }
            val toCurrencyRate = rates.find { currency -> currency.currency == toCurrencyCode }

            if (fromCurrencyRate == null) {
                throw DomainError.CurrencyRateNotFound(fromCurrencyCode)
            } else if (toCurrencyRate == null) {
                throw DomainError.CurrencyRateNotFound(toCurrencyCode)
            }

            applyConversionRateToAmountUseCase.execute(
                amount,
                fromCurrencyRate.rate,
                toCurrencyRate.rate
            )
        }
}
