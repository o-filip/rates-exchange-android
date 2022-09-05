package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.core.entity.InternetConnectionStatus
import com.ofilip.exchange_rates.core.network.ConnectivityStatusHelper
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetInternetConnectionStatusUseCase {
    fun execute(): Flow<Result<InternetConnectionStatus>>
}

class GetInternetConnectionStatusUseCaseImpl @Inject constructor(
    private val connectivityStatusHelper: ConnectivityStatusHelper,
    private val currencyRateRepository: CurrencyRateRepository
) : GetInternetConnectionStatusUseCase {
    override fun execute(): Flow<Result<InternetConnectionStatus>> =
        connectivityStatusHelper.isConnected
            .combine(currencyRateRepository.lastCurrencyRateLoadTimestampMs) { isConnected, lastDataLoad ->
                lastDataLoad.map {
                    InternetConnectionStatus(
                        isConnected = isConnected,
                        lastDataLoadedTimestampMs = it
                    )
                }
            }
}
