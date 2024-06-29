package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.core.error.DomainError
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case to get a currency by its code
 *
 */
abstract class GetCurrencyUseCase {

    abstract fun execute(currencyCode: String): Flow<Currency>
}

class GetCurrencyUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : GetCurrencyUseCase() {
    override fun execute(currencyCode: String): Flow<Currency> =
        currencyRepository.getCurrency(currencyCode).map { currency ->
            currency ?: throw DomainError.CurrencyNotFound(currencyCode)
        }

}
