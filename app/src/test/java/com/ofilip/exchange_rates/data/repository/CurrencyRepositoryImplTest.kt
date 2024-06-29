package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.core.error.DataError
import com.ofilip.exchange_rates.data.convert.CurrencyConverter
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyLocalDataStore
import com.ofilip.exchange_rates.data.remote.dataStore.CurrencyRemoteDataStore
import com.ofilip.exchange_rates.fixtures.Fixtures
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

class CurrencyRepositoryImplTest {

    private val remoteDataStore: CurrencyRemoteDataStore = mock()
    private val currencyLocalDataStore: CurrencyLocalDataStore = mock()
    private val currencyConverter: CurrencyConverter = mock()

    private val currencyRepository = CurrencyRepositoryImpl(
        remoteDataStore,
        currencyLocalDataStore,
        currencyConverter
    )

    private val remoteResponse = Fixtures.currenciesRemoteModels

    private val currencyLocalModels = Fixtures.currencies


    @Test
    fun `prefetchCurrencies should insert currencies when remote data is available`() =
        runBlocking {
            // Given
            remoteDataStore.stub { onBlocking { getAllCurrencies() } doReturn remoteResponse }
            currencyLocalDataStore.stub { onBlocking { getAll() } doReturn flowOf(emptyList()) }
            currencyConverter.stub { onBlocking { remoteToEntity(remoteResponse[0]) } doReturn currencyLocalModels[0] }
            currencyConverter.stub { onBlocking { remoteToEntity(remoteResponse[1]) } doReturn currencyLocalModels[1] }
            currencyConverter.stub { onBlocking { remoteToEntity(remoteResponse[2]) } doReturn currencyLocalModels[2] }
            currencyLocalDataStore.stub { onBlocking { insert(currencyLocalModels) } doReturn Unit }

            // When
            val result = currencyRepository.prefetchCurrencies()

            // Then
            verify(remoteDataStore).getAllCurrencies()
            verify(currencyLocalDataStore).getAll()
            verify(currencyConverter).remoteToEntity(remoteResponse[0])
            verify(currencyConverter).remoteToEntity(remoteResponse[1])
            verify(currencyConverter).remoteToEntity(remoteResponse[2])
            verify(currencyLocalDataStore).insert(currencyLocalModels)
            assertEquals(Unit, result)
        }

    @Test
    fun `prefetchCurrencies should return failure result when an exception is thrown`() =
        runBlocking {
            val exception = DataError.Unknown()

            remoteDataStore.stub {
                onBlocking { getAllCurrencies() } doAnswer { throw exception }
            }

            val thrownException = assertThrows<DataError.Unknown> {
                currencyRepository.prefetchCurrencies()
            }

            verify(remoteDataStore).getAllCurrencies()
            assertEquals(exception, thrownException)
        }

    @Test
    fun `getCurrencies should return currencies from local data store`() = runBlocking {
        val textQuery = ""
        val onlyFavorites = false

        currencyLocalDataStore.stub {
            onBlocking { getAll(any(), any()) } doReturn flowOf(currencyLocalModels)
        }


        val result = currencyRepository.getCurrencies(textQuery, onlyFavorites).first()

        verify(currencyLocalDataStore).getAll(textQuery, onlyFavorites)
        assertEquals(currencyLocalModels, result)
    }

    @Test
    fun `areCurrenciesLoaded should return true if currencies are loaded in local data store`() =
        runBlocking {
            currencyLocalDataStore.stub {
                onBlocking { getAll() } doReturn flowOf(currencyLocalModels)
            }

            val result = currencyRepository.areCurrenciesLoaded()

            assertEquals(true, result)
        }

    @Test
    fun `areCurrenciesLoaded should return false if currencies are not loaded in local data store`() =
        runBlocking {
            currencyLocalDataStore.stub {
                onBlocking { getAll() } doReturn flowOf(emptyList())
            }

            val result = currencyRepository.areCurrenciesLoaded()

            verify(currencyLocalDataStore).getAll()
            assertEquals(false, result)
        }

    @Test
    fun `setOverviewBaseCurrency should update the overview base currency in local data store`() =
        runBlocking {
            val currencyCode = "USD"

            currencyRepository.setOverviewBaseCurrency(currencyCode)

            verify(currencyLocalDataStore).setOverviewBaseCurrency(currencyCode)
        }

    @Test
    fun `setConversionCurrencyFrom should update the conversion currency from in local data store`() =
        runBlocking {
            val currencyCode = "USD"

            currencyRepository.setConversionCurrencyFrom(currencyCode)

            verify(currencyLocalDataStore).setConversionCurrencyFrom(currencyCode)
        }

    @Test
    fun `setConversionCurrencyTo should update the conversion currency to in local data store`() =
        runBlocking {
            val currencyCode = "USD"

            currencyRepository.setConversionCurrencyTo(currencyCode)

            verify(currencyLocalDataStore).setConversionCurrencyTo(currencyCode)
        }

    @Test
    fun `overviewBaseCurrency should return flow of overview base currency`() = runBlocking {
        val currency = "USD"

        currencyLocalDataStore.stub { onBlocking { overviewBaseCurrency } doReturn flowOf(currency) }

        val result = currencyRepository.overviewBaseCurrency.first()

        assertEquals(currency, result)
    }

    @Test
    fun `conversionCurrencyFrom should return flow of conversion currency from`() = runBlocking {
        val currency = "USD"

        currencyLocalDataStore.stub { onBlocking { conversionCurrencyFrom } doReturn flowOf(currency) }

        val result = currencyRepository.conversionCurrencyFrom.first()

        assertEquals(currency, result)
    }

    @Test
    fun `conversionCurrencyTo should return flow of conversion currency to`() = runBlocking {
        val currency = "USD"

        currencyLocalDataStore.stub { onBlocking { conversionCurrencyTo } doReturn flowOf(currency) }

        val result = currencyRepository.conversionCurrencyTo.first()

        assertEquals(currency, result)
    }

}
