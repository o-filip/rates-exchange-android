package com.ofilip.exchange_rates.domain.useCase.rateTimeSeries

import com.ofilip.exchange_rates.core.entity.RatesTimeSeriesItem
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class GetRatesTimeSeriesUseCaseImplTest {
    private val mockCurrencyRateRepository: CurrencyRateRepository = mock()
    private val useCase = GetRatesTimeSeriesUseCaseImpl(
        currencyRateRepository = mockCurrencyRateRepository
    )

    @Test
    fun `execute should return rates time series from repository`() = runBlocking {
        // Given
        val startDate = DateTime.parse("2023-01-01")
        val endDate = DateTime.parse("2023-01-10")
        val baseCurrencyCode = "USD"
        val currencyCode = "EUR"
        val mockRatesTimeSeries = listOf(
            RatesTimeSeriesItem(DateTime.parse("2023-01-01"), mapOf("EUR" to 1.2)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-02"), mapOf("EUR" to 1.3)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-03"), mapOf("EUR" to 1.4))
        )

        mockCurrencyRateRepository.stub {
            onBlocking {
                getRatesTimeSeries(
                    startDate,
                    endDate,
                    baseCurrencyCode,
                    listOf(currencyCode)
                )
            } doReturn mockRatesTimeSeries
        }

        // When
        val result = useCase.execute(startDate, endDate, baseCurrencyCode, currencyCode)

        // Then
        verify(mockCurrencyRateRepository).getRatesTimeSeries(
            startDate,
            endDate,
            baseCurrencyCode,
            listOf(currencyCode)
        )

        assertEquals(result, mockRatesTimeSeries)
    }

    @Test
    fun `execute should return failure when repository returns error`() = runBlocking {
        // Given
        val startDate = DateTime.parse("2023-01-01")
        val endDate = DateTime.parse("2023-01-10")
        val baseCurrencyCode = "USD"
        val currencyCode = "EUR"
        val exception = RuntimeException("Failed to get rates time series")

        mockCurrencyRateRepository.stub {
            onBlocking {
                getRatesTimeSeries(
                    startDate,
                    endDate,
                    baseCurrencyCode,
                    listOf(currencyCode)
                )
            }.thenThrow(exception)
        }

        // When
        val thrownException = assertThrows<RuntimeException> {
            useCase.execute(startDate, endDate, baseCurrencyCode, currencyCode)
        }


        // Then
        verify(mockCurrencyRateRepository).getRatesTimeSeries(
            startDate,
            endDate,
            baseCurrencyCode,
            listOf(currencyCode)
        )
        assertEquals(exception, thrownException)
    }
}
