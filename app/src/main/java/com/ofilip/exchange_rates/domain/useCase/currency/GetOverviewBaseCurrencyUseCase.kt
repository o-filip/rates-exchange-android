package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface GetOverviewBaseCurrencyUseCase {
    fun execute(): Flow<String>
}

class GetOverviewBaseCurrencyUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : GetOverviewBaseCurrencyUseCase {
    override fun execute(): Flow<String> = currencyRepository.overviewBaseCurrency
}
