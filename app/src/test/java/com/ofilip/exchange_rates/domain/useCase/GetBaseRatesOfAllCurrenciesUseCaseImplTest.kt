package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.fixtures.Fixtures
import com.ofilip.exchange_rates.fixtures.toFlowOfSuccess
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class GetBaseRatesOfAllCurrenciesUseCaseTest {

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
                onBlocking { getCurrencies() }.thenReturn(flowOf(Result.success(emptyList())))
            }
            mockCurrencyRateRepository.stub {
                onBlocking { getRates(any()) }.thenReturn(flowOf(Result.success(emptyList())))
            }
            val result = useCase.execute().first()

            verify(mockCurrencyRepository).getCurrencies()
            verify(mockCurrencyRateRepository).getRates(emptyList())
            assertEquals(Result.success(emptyList<List<CurrencyRate>>()), result)
        }


    @Test
    fun `execute should return list of currency rates of all currencies`() = runBlocking {
        val currencies = Fixtures.currencies
        val localRates = Fixtures.localRates
        val currencyCodes = Fixtures.currencyCodes

        mockCurrencyRepository.stub {
            onBlocking { getCurrencies() }.thenReturn(currencies.toFlowOfSuccess())
        }
        mockCurrencyRateRepository.stub {
            onBlocking { getRates(any()) }.thenReturn(localRates.toFlowOfSuccess())
        }
        val result = useCase.execute().first()

        verify(mockCurrencyRepository).getCurrencies()
        verify(mockCurrencyRateRepository).getRates(currencyCodes)
        assertEquals(Result.success(localRates), result)
    }

    @Test
    fun `execute should return failure when getCurrencies fails`() = runBlocking {
        val exception = Exception("Failed to get currencies")
        mockCurrencyRepository.stub {
            onBlocking { getCurrencies() }.thenReturn(flowOf(Result.failure(exception)))
        }

        val result = useCase.execute().first()

        verify(mockCurrencyRepository).getCurrencies()
        assertEquals(Result.failure<List<CurrencyRate>>(exception), result)
    }

    @Test
    fun `execute should return failure when getRates fails`() = runBlocking {
        val exception = Exception("Failed to get currency rates")
        val currencies = Fixtures.currencies
        val currencyCodes = Fixtures.currencyCodes
        mockCurrencyRepository.stub {
            onBlocking { getCurrencies() }.thenReturn(currencies.toFlowOfSuccess())
        }
        mockCurrencyRateRepository.stub {
            onBlocking { getRates(any()) }.thenReturn(flowOf(Result.failure(exception)))
        }
        val result = useCase.execute().first()

        verify(mockCurrencyRepository).getCurrencies()
        verify(mockCurrencyRateRepository).getRates(currencyCodes)
        assertEquals(Result.failure<List<CurrencyRate>>(exception), result)
    }

}