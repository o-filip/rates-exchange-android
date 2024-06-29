package com.ofilip.exchange_rates.domain.useCase.conversion

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.core.error.DomainError
import com.ofilip.exchange_rates.domain.useCase.rate.GetBaseRatesOfAllCurrenciesUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.stub

class ConvertCurrencyUseCaseTest {

    private val mockConversionResult = 72.0

    private val mockRatesFlow = flowOf(
        listOf(
            CurrencyRate("USD", 1.0),
            CurrencyRate("EUR", 0.85),
            CurrencyRate("GBP", 0.72),
            CurrencyRate("NONE", null),
        )
    )

    private val mockGetRatesOfAllCurrenciesUseCase: GetBaseRatesOfAllCurrenciesUseCase = mock()
    private val mockApplyConversionRateToAmountUseCase: ApplyConversionRateToAmountUseCase =
        object :
            ApplyConversionRateToAmountUseCase {
            override fun execute(
                amount: Double,
                origToBaseCurrencyRate: Double?,
                baseToTargetCurrencyRate: Double?
            ): Double? =
                if (origToBaseCurrencyRate == null || baseToTargetCurrencyRate == null) {
                    null
                } else {
                    mockConversionResult
                }
        }


    private val useCase: ConvertCurrencyUseCase = ConvertCurrencyUseCaseImpl(
        mockGetRatesOfAllCurrenciesUseCase,
        mockApplyConversionRateToAmountUseCase
    )

    init {
        mockGetRatesOfAllCurrenciesUseCase.stub {
            onBlocking { execute() }.thenReturn(mockRatesFlow)
        }
    }

    @Test
    fun `convert should return CurrencyRateNotFoundError when fromCurrencyCode is not found`() =
        runBlocking {
            val amount = 100.0
            val fromCurrencyCode = "CAD"
            val toCurrencyCode = "USD"

            val thrownException = assertThrows<DomainError.CurrencyRateNotFound> {
                useCase.execute(amount, fromCurrencyCode, toCurrencyCode)
            }

            assertEquals(
                DomainError.CurrencyRateNotFound(fromCurrencyCode),
                thrownException
            )
        }

    @Test
    fun `convert should return CurrencyRateNotFoundError when toCurrencyCode is not found`() =
        runBlocking {
            val amount = 100.0
            val fromCurrencyCode = "USD"
            val toCurrencyCode = "CAD"

            val thrownException = assertThrows<DomainError.CurrencyRateNotFound> {
                useCase.execute(amount, fromCurrencyCode, toCurrencyCode)
            }

            assertEquals(
                DomainError.CurrencyRateNotFound(toCurrencyCode),
                thrownException
            )
        }

    @Test
    fun `convert should return null when rates are missing`() =
        runBlocking {
            val amount = 100.0
            val fromCurrencyCode = "NONE"
            val toCurrencyCode = "EUR"

            val result = useCase.execute(amount, fromCurrencyCode, toCurrencyCode)

            assertEquals(null, result)
        }


    @Test
    fun `convert should calculate converted amount when rates are available`() {
        runBlocking {
            val amount = 100.0
            val fromCurrencyCode = "USD"
            val toCurrencyCode = "GBP"

            val result = useCase.execute(amount, fromCurrencyCode, toCurrencyCode)

            assertEquals(mockConversionResult, result)
        }
    }

}
