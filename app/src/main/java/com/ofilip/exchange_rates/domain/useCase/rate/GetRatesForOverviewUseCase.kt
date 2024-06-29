package com.ofilip.exchange_rates.domain.useCase.rate

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.core.extensions.flatMapLatestResult
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.domain.useCase.conversion.ApplyConversionRateToAmountUseCase
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case to get the rates of all currencies relative to the overview base currency used
 * in the overview screen
 */
interface GetRatesForOverviewUseCase {
    fun execute(): Flow<Result<List<CurrencyRate>>>
}

class GetRatesForOverviewUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val getRatesOfAllCurrenciesUseCase: GetBaseRatesOfAllCurrenciesUseCase,
    private val applyConversionRateToAmountUseCase: ApplyConversionRateToAmountUseCase,
) : GetRatesForOverviewUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<Result<List<CurrencyRate>>> {
        return currencyRepository.overviewBaseCurrency
            .flatMapLatestResult { overviewCurrency ->
                getRatesOfAllCurrenciesUseCase.execute().map { flow ->
                    flow.map { rates -> convertCurrencyRates(rates, overviewCurrency) }
                }
            }
    }

    private fun convertCurrencyRates(
        rates: List<CurrencyRate>,
        overviewCurrency: String
    ): List<CurrencyRate> {
        val baseToOverviewCurrencyRate = rates.find { currency -> currency.currency == overviewCurrency }
        return rates.map { rate ->
            val result = applyConversionRateToAmountUseCase.execute(
                amount = 1.0,
                origToBaseCurrencyRate = rate.rate,
                baseToTargetCurrencyRate = baseToOverviewCurrencyRate?.rate
            )

            rate.copy(rate = result)
        }
    }

}
