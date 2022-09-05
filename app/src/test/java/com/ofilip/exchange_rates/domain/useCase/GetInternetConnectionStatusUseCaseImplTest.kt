package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.core.network.ConnectivityStatusHelper
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.stub

@ExperimentalCoroutinesApi
class GetInternetConnectionStatusUseCaseTest {


    private val mockConnectivityStatusHelper: ConnectivityStatusHelper = mock()
    private val mockCurrencyRateRepository: CurrencyRateRepository = mock()

    private val useCase = GetInternetConnectionStatusUseCaseImpl(
        mockConnectivityStatusHelper,
        mockCurrencyRateRepository
    )

    @Test
    fun `execute should return InternetConnectionStatus with correct values`() = runBlocking {
        // Given
        val isConnectedVal = true
        val lastDataLoadedTimestampMs = 1623254700000L
        mockConnectivityStatusHelper.stub {
            onBlocking { isConnected }.thenReturn(MutableStateFlow(isConnectedVal))
        }
        mockCurrencyRateRepository.stub {
            onBlocking { lastCurrencyRateLoadTimestampMs }.thenReturn(
                flowOf(Result.success(lastDataLoadedTimestampMs))
            )
        }

        // When
        val result = useCase.execute().first()

        // Then
        assertEquals(isConnectedVal, result.getOrNull()?.isConnected)
        assertEquals(lastDataLoadedTimestampMs, result.getOrNull()?.lastDataLoadedTimestampMs)
    }
}


