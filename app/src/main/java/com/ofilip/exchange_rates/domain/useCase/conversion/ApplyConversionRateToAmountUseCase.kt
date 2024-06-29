package com.ofilip.exchange_rates.domain.useCase.conversion

import javax.inject.Inject


interface ApplyConversionRateToAmountUseCase {
    /**
     * Applies the conversion rates to the amount
     *
     * @param amount the amount to be converted
     * @param origToBaseCurrencyRate the rate of the original currency to the base currency
     * @param baseToTargetCurrencyRate the rate of the base currency to the target currency
     * @return null if any of the rates is null or 0.0, otherwise the converted amount
     */
    fun execute(
        amount: Double,
        origToBaseCurrencyRate: Double?,
        baseToTargetCurrencyRate: Double?
    ): Double?
}

class ApplyConversionRateToAmountUseCaseImpl @Inject constructor() :
    ApplyConversionRateToAmountUseCase {
    override fun execute(
        amount: Double,
        origToBaseCurrencyRate: Double?,
        baseToTargetCurrencyRate: Double?
    ): Double? =
        if (origToBaseCurrencyRate == null
            || origToBaseCurrencyRate == 0.0
            || baseToTargetCurrencyRate == null
            || baseToTargetCurrencyRate == 0.0
        ) {
            null
        } else {
            amount / origToBaseCurrencyRate * baseToTargetCurrencyRate
        }
}
