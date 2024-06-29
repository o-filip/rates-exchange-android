package com.ofilip.exchange_rates.data.convert

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import javax.inject.Inject

interface CurrencyRateConverter {
    fun remoteToEntity(
        currencyCode: String,
        rate: Double?
    ): CurrencyRate
}

class CurrencyRateConverterImpl @Inject constructor() : CurrencyRateConverter {
    override fun remoteToEntity(currencyCode: String, rate: Double?): CurrencyRate =
        CurrencyRate(
            currency = currencyCode,
            rate = rate
        )
}
