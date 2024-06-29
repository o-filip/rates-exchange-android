package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.core.entity.RatesTimeSeriesItem
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import javax.inject.Inject
import org.joda.time.DateTime

interface GetRatesTimeSeriesUseCase {
    suspend fun execute(
        startDate: DateTime,
        endDate: DateTime,
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): Result<List<RatesTimeSeriesItem>>
}

class GetRatesTimeSeriesUseCaseImpl @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository,
) : GetRatesTimeSeriesUseCase {
    override suspend fun execute(
        startDate: DateTime,
        endDate: DateTime,
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): Result<List<RatesTimeSeriesItem>> =
        currencyRateRepository.getRatesTimeSeries(
            startDate,
            endDate,
            baseCurrencyCode,
            currencyCodes
        )

}