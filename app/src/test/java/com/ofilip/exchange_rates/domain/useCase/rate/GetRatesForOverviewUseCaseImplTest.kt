package com.ofilip.exchange_rates.domain.useCase.rate

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.domain.useCase.conversion.ApplyConversionRateToAmountUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class GetRatesForOverviewUseCaseImplTest {

    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val mockGetRatesOfAllCurrenciesUseCase: GetBaseRatesOfAllCurrenciesUseCase = mock()
    private val mockApplyConversionRateToAmountUseCase: ApplyConversionRateToAmountUseCase = mock()
    private val useCase = GetRatesForOverviewUseCaseImpl(
        mockCurrencyRepository,
        mockGetRatesOfAllCurrenciesUseCase,
        mockApplyConversionRateToAmountUseCase,
        UnconfinedTestDispatcher()
    )

    @Test
    fun `execute should return converted currency rates`() = runBlocking {
        // Given
        val mockOverviewCurrency = "USD"
        val mockBaseToOverviewCurrencyRate = 2.0
        val mockRates = listOf(
            CurrencyRate("USD", mockBaseToOverviewCurrencyRate),
            CurrencyRate("GBP", 0.8),
            CurrencyRate("JPY", 125.5)
        )

        mockCurrencyRepository.stub {
            onBlocking { overviewBaseCurrency }.thenReturn(flowOf(mockOverviewCurrency))
        }
        mockGetRatesOfAllCurrenciesUseCase.stub {
            onBlocking { execute() }.thenReturn(flowOf(mockRates))
        }

        mockApplyConversionRateToAmountUseCase.stub {
            onBlocking {
                execute(
                    any(),
                    eq(mockBaseToOverviewCurrencyRate),
                    eq(mockBaseToOverviewCurrencyRate)
                )
            }.thenReturn(1.0)
            onBlocking {
                execute(any(), eq(0.8), eq(mockBaseToOverviewCurrencyRate))
            }.thenReturn(0.8 * mockBaseToOverviewCurrencyRate)
            onBlocking {
                execute(any(), eq(125.5), eq(mockBaseToOverviewCurrencyRate))
            }.thenReturn(125.5 * mockBaseToOverviewCurrencyRate)
        }

        // When
        val result = useCase.execute().first()

        // Then
        verify(mockCurrencyRepository).overviewBaseCurrency
        verify(mockGetRatesOfAllCurrenciesUseCase).execute()
        verify(mockApplyConversionRateToAmountUseCase)
            .execute(1.0, mockBaseToOverviewCurrencyRate, mockBaseToOverviewCurrencyRate)
        verify(mockApplyConversionRateToAmountUseCase)
            .execute(1.0, 0.8, mockBaseToOverviewCurrencyRate)
        verify(mockApplyConversionRateToAmountUseCase)
            .execute(1.0, 125.5, mockBaseToOverviewCurrencyRate)

        assertEquals(result[0], CurrencyRate("USD", 1.0))
        assertEquals(
            result[1],
            CurrencyRate("GBP", 0.8 * mockBaseToOverviewCurrencyRate)
        )
        assertEquals(
            result[2],
            CurrencyRate("JPY", 125.5 * mockBaseToOverviewCurrencyRate)
        )
    }

    @Test
    fun `execute should return failure when overviewBaseCurrency fails`() = runBlocking {
        // Given
        val mockError = RuntimeException("Failed to fetch overview base currency")

        mockCurrencyRepository.stub {
            onBlocking { overviewBaseCurrency }.thenThrow(mockError)
        }

        // When
        val thrownException = assertThrows<RuntimeException> {
            useCase.execute().first()
        }

        // Then
        verify(mockCurrencyRepository).overviewBaseCurrency
        assertEquals(thrownException, mockError)
    }

    @Test
    fun `execute should return failure when getRatesOfAllCurrenciesUseCase fails`() = runBlocking {
        // Given
        val mockOverviewCurrency = "USD"
        val mockError = RuntimeException("Failed to fetch currency rates")

        mockCurrencyRepository.stub {
            onBlocking { overviewBaseCurrency }.thenReturn(flowOf(mockOverviewCurrency))
        }
        mockGetRatesOfAllCurrenciesUseCase.stub {
            onBlocking { execute() }.thenThrow(mockError)
        }

        // When
        val thrownException = assertThrows<RuntimeException> {
            useCase.execute().first()
        }

        // Then
        verify(mockCurrencyRepository).overviewBaseCurrency
        verify(mockGetRatesOfAllCurrenciesUseCase).execute()

        assertEquals(thrownException.message, mockError.message)
    }
}
