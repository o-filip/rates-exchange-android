package com.ofilip.exchange_rates.domain.useCase.rate

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.fixtures.Fixtures
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class GetBaseRatesOfAllCurrenciesUseCaseImplTest {

    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val mockCurrencyRateRepository: CurrencyRateRepository = mock()

    private val useCase: GetBaseRatesOfAllCurrenciesUseCase =
        GetBaseRatesOfAllCurrenciesUseCaseImpl(
            mockCurrencyRepository,
            mockCurrencyRateRepository
        )

    @Test
    fun `execute should return empty list of currency rates if there are no currencies`() =
        runBlocking {
            mockCurrencyRepository.stub {
                onBlocking { getCurrencies() }.thenReturn(flowOf(emptyList()))
            }
            mockCurrencyRateRepository.stub {
                onBlocking { getRates(any()) }.thenReturn(flowOf(emptyList()))
            }
            val result = useCase.execute().first()

            verify(mockCurrencyRepository).getCurrencies()
            verify(mockCurrencyRateRepository).getRates(emptyList())
            assertEquals(emptyList<List<CurrencyRate>>(), result)
        }


    @Test
    fun `execute should return list of currency rates of all currencies`() = runBlocking {
        val currencies = Fixtures.currencies
        val localRates = Fixtures.localRates
        val currencyCodes = Fixtures.currencyCodes

        mockCurrencyRepository.stub {
            onBlocking { getCurrencies() }.thenReturn(flowOf(currencies))
        }
        mockCurrencyRateRepository.stub {
            onBlocking { getRates(any()) }.thenReturn(flowOf(localRates))
        }
        val result = useCase.execute().first()

        verify(mockCurrencyRepository).getCurrencies()
        verify(mockCurrencyRateRepository).getRates(currencyCodes)
        assertEquals(localRates, result)
    }

    @Test
    fun `execute should return failure when getCurrencies fails`() = runBlocking {
        val exception = RuntimeException("Failed to get currencies")
        mockCurrencyRepository.stub {
            onBlocking { getCurrencies() }.thenThrow(exception)
        }

        val thrownException = assertThrows<RuntimeException> {
            useCase.execute().first()
        }

        verify(mockCurrencyRepository).getCurrencies()
        assertEquals(exception, thrownException)
    }

    @Test
    fun `execute should return failure when getRates fails`() = runBlocking {
        val exception = RuntimeException("Failed to get currency rates")
        val currencies = Fixtures.currencies
        val currencyCodes = Fixtures.currencyCodes
        mockCurrencyRepository.stub {
            onBlocking { getCurrencies() }.thenReturn(flowOf(currencies))
        }
        mockCurrencyRateRepository.stub {
            onBlocking { getRates(any()) }.thenThrow(exception)
        }

        val thrownException = assertThrows<Exception> {
            useCase.execute().first()
        }


        verify(mockCurrencyRepository).getCurrencies()
        verify(mockCurrencyRateRepository).getRates(currencyCodes)
        assertEquals(exception.message, thrownException.message)
    }

}
