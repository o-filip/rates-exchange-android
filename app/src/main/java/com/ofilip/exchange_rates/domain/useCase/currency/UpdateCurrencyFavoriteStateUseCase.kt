package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import javax.inject.Inject

interface UpdateCurrencyFavoriteStateUseCase {
    suspend fun execute(currency: Currency, isFavorite: Boolean): Result<Unit>
}

class UpdateCurrencyFavoriteStateUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) : UpdateCurrencyFavoriteStateUseCase {
    override suspend fun execute(currency: Currency, isFavorite: Boolean): Result<Unit> =
        currencyRepository.updateCurrencyFavoriteState(currency, isFavorite)
}
