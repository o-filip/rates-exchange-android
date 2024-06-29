package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.core.entity.InternetConnectionStatus
import com.ofilip.exchange_rates.core.network.ConnectivityStatusHelper
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Use case to get the internet connection status and the timestamp of the last data load
 */
interface GetInternetConnectionStatusUseCase {
    fun execute(): Flow<InternetConnectionStatus>
}

class GetInternetConnectionStatusUseCaseImpl @Inject constructor(
    private val connectivityStatusHelper: ConnectivityStatusHelper,
    private val currencyRateRepository: CurrencyRateRepository
) : GetInternetConnectionStatusUseCase {
    override fun execute(): Flow<InternetConnectionStatus> =
        connectivityStatusHelper.isConnected
            .combine(currencyRateRepository.lastCurrencyRateLoadTimestampMs) { isConnected, lastDataLoad ->
                InternetConnectionStatus(
                    isConnected = isConnected,
                    lastDataLoadedTimestampMs = lastDataLoad
                )
            }
}
