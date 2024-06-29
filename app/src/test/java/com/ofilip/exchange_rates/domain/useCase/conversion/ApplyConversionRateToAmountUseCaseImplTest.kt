package com.ofilip.exchange_rates.domain.useCase.conversion

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApplyConversionRateToAmountUseCaseImplTest {

    private val useCase: ApplyConversionRateToAmountUseCase = ApplyConversionRateToAmountUseCaseImpl()

    @Test
    fun `execute should return null when origToBaseCurrencyRate is null`() {
        val amount = 100.0
        val origToBaseCurrencyRate: Double? = null
        val baseToTargetCurrencyRate = 1.5

        val result = useCase.execute(amount, origToBaseCurrencyRate, baseToTargetCurrencyRate)

        assertEquals(null, result)
    }

    @Test
    fun `execute should return null when origToBaseCurrencyRate is 0`() {
        val amount = 100.0
        val origToBaseCurrencyRate = 0.0
        val baseToTargetCurrencyRate = 1.5

        val result = useCase.execute(amount, origToBaseCurrencyRate, baseToTargetCurrencyRate)

        assertEquals(null, result)
    }

    @Test
    fun `execute should return null when baseToTargetCurrencyRate is null`() {
        val amount = 100.0
        val origToBaseCurrencyRate = 2.0
        val baseToTargetCurrencyRate: Double? = null

        val result = useCase.execute(amount, origToBaseCurrencyRate, baseToTargetCurrencyRate)

        assertEquals(null, result)
    }

    @Test
    fun `execute should return null when baseToTargetCurrencyRate is 0`() {
        val amount = 100.0
        val origToBaseCurrencyRate = 2.0
        val baseToTargetCurrencyRate = 0.0

        val result = useCase.execute(amount, origToBaseCurrencyRate, baseToTargetCurrencyRate)

        assertEquals(null, result)
    }

    @Test
    fun `execute should calculate converted amount when rates are valid`() {
        val amount = 100.0
        val origToBaseCurrencyRate = 2.0
        val baseToTargetCurrencyRate = 1.5
        val expectedConvertedAmount = 75.0

        val result = useCase.execute(amount, origToBaseCurrencyRate, baseToTargetCurrencyRate)

        assertEquals(expectedConvertedAmount, result)
    }
}
