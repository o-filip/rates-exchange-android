package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.core.error.DataError
import com.ofilip.exchange_rates.core.network.ConnectivityStatusHelper
import com.ofilip.exchange_rates.data.convert.CurrencyRateConverter
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyLocalDataStore
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyRateLocalDataStore
import com.ofilip.exchange_rates.data.remote.dataStore.CurrencyRemoteDataStore
import com.ofilip.exchange_rates.fixtures.Fixtures
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class CurrencyRateRepositoryImplTest {

    private val remoteDataStore: CurrencyRemoteDataStore = mock()
    private val currencyLocalDataStore: CurrencyLocalDataStore = mock()
    private val currencyRateLocalDataStore: CurrencyRateLocalDataStore = mock()
    private val connectivityStatusHelper: ConnectivityStatusHelper = mock()
    private val currencyRateConverter: CurrencyRateConverter = mock()

    private val currencyRemoteFetchLimitMs: Long = 1000L
    private val baseCurrencyCode: String = "EUR"

    private val repository = CurrencyRateRepositoryImpl(
        remoteDataStore,
        currencyRateLocalDataStore,
        connectivityStatusHelper,
        currencyRateConverter,
        baseCurrency = baseCurrencyCode,
        currencyRemoteFetchLimitMs = currencyRemoteFetchLimitMs
    )

    @Test
    fun `getRates loads data from remote store them in local data store and then return it from local data store if device is online`() =
        runBlocking {
            // Given
            val connectivityStatusFlow = MutableStateFlow(true)
            val currencies = Fixtures.currencies
            val localRates = Fixtures.localRates
            val remoteRates = Fixtures.remoteRates
            val remoteRatesEntries = Fixtures.remoteRatesEntries
            val currencyCodes = Fixtures.currencyCodes


            connectivityStatusHelper.stub {
                on { isConnected } doReturn connectivityStatusFlow
            }
            currencyLocalDataStore.stub {
                onBlocking { getAll() } doReturn flowOf(currencies)
            }
            currencyRateLocalDataStore.stub {
                onBlocking { lastRatesLoadTimestampMs } doReturn flowOf(0)
            }
            currencyRateLocalDataStore.stub {
                onBlocking { getAll() } doReturn flowOf(localRates)
            }
            remoteDataStore.stub {
                onBlocking { getLatestRates(any(), any()) } doReturn remoteRates
            }
            currencyRateConverter.stub {
                on { remoteToEntity(remoteRatesEntries[0].key, remoteRatesEntries[0].value) } doReturn localRates[0]
                on { remoteToEntity(remoteRatesEntries[1].key, remoteRatesEntries[1].value) } doReturn localRates[1]
                on { remoteToEntity(remoteRatesEntries[2].key, remoteRatesEntries[2].value) } doReturn localRates[2]
            }

            // When
            val result = repository.getRates(currencyCodes).first()

            // Then
            verify(connectivityStatusHelper).isConnected
            verify(remoteDataStore).getLatestRates(baseCurrencyCode, currencyCodes)
            verify(currencyRateConverter).remoteToEntity(remoteRatesEntries[0].key, remoteRatesEntries[0].value)
            verify(currencyRateConverter).remoteToEntity(remoteRatesEntries[1].key, remoteRatesEntries[1].value)
            verify(currencyRateConverter).remoteToEntity(remoteRatesEntries[2].key, remoteRatesEntries[2].value)
            verify(currencyRateLocalDataStore).deleteAll()
            verify(currencyRateLocalDataStore).insert(localRates)
            verify(currencyRateLocalDataStore).getAll()
            assertEquals(Result.success(localRates), result)
        }

    @Test
    fun `getRates loads data from local store if device is offline`() = runBlocking {
        // Given
        val connectivityStatusFlow = MutableStateFlow(false)
        val localRates = Fixtures.localRates
        val currencyCodes = Fixtures.currencyCodes

        connectivityStatusHelper.stub {
            on { isConnected } doReturn connectivityStatusFlow
        }
        currencyRateLocalDataStore.stub {
            onBlocking { getAll() } doReturn flowOf(localRates)
        }

        // When
        val result = repository.getRates(currencyCodes).first()

        // Then
        verify(currencyRateLocalDataStore).getAll()
        verify(connectivityStatusHelper).isConnected
        assertEquals(Result.success(localRates), result)
    }

    @Test
    fun `getRates should return failure when remote data store throws an exception`() =
        runBlocking {
            // Given
            val exception = Exception()
            val connectivityStatusFlow = MutableStateFlow(true)
            val currencyCodes = Fixtures.currencyCodes
            connectivityStatusHelper.stub {
                on { isConnected } doReturn connectivityStatusFlow
            }
            currencyRateLocalDataStore.stub {
                onBlocking { lastRatesLoadTimestampMs } doReturn flowOf(0)
            }
            remoteDataStore.stub {
                onBlocking { getLatestRates(any(), any()) }.doAnswer {
                    throw exception
                }
            }

            // When
            val result: Result<List<CurrencyRate>> = repository.getRates(currencyCodes).first()

            // Then
            verify(connectivityStatusHelper).isConnected
            verify(remoteDataStore).getLatestRates(baseCurrencyCode, currencyCodes)
            assertEquals(Result.failure<List<CurrencyRate>>(DataError.Unknown(exception)), result)
        }

    @Test
    fun `getRates loads data from local if the last currencies remote fetch was less than the limit`() =
        runBlocking {
            // Given
            val connectivityStatusFlow = MutableStateFlow(true)
            val localRates = Fixtures.localRates
            val currencyCodes = Fixtures.currencyCodes
            connectivityStatusHelper.stub {
                on { isConnected } doReturn connectivityStatusFlow
            }
            currencyRateLocalDataStore.stub {
                onBlocking { lastRatesLoadTimestampMs } doReturn flowOf(System.currentTimeMillis())
            }
            currencyRateLocalDataStore.stub {
                onBlocking { getAll() } doReturn flowOf(localRates)
            }

            // When
            val result = repository.getRates(currencyCodes).first()

            // Then
            verify(connectivityStatusHelper).isConnected
            verify(currencyRateLocalDataStore).getAll()
            assertEquals(Result.success(localRates), result)
        }
}