package com.ofilip.exchange_rates.data.convert

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CurrencyRateConverterImplTest {

    private val converter: CurrencyRateConverter = CurrencyRateConverterImpl()

    @Test
    fun testRemoteToEntity() {
        // Given
        val currency = "USD"
        val rate = 1.0

        // When
        val entityRate = converter.remoteToEntity(currency, rate)

        // Then
        assertEquals(currency, entityRate.currency)
        assertEquals(rate, entityRate.rate)
    }
}
