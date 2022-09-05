package com.ofilip.exchange_rates.data.convert

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CurrencyConverterImplTest {

    private lateinit var currencyConverter: CurrencyConverter

    @BeforeEach
    fun setUp() {
        currencyConverter = CurrencyConverterImpl()
    }

    @Test
    fun `test remoteToEntity`() {
        val remoteCurrency = CurrencyRemoteModel(
            currencyCode = "USD",
            name = "United States Dollar",
            numberCode = "840",
            precision = 2,
            symbol = "$",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
        )

        val expectedEntity = Currency(
            currencyCode = "USD",
            currencyName = "United States Dollar",
            numberCode = "840",
            precision = 2,
            symbol = "$",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
            isFavorite = false
        )

        val entity = currencyConverter.remoteToEntity(remoteCurrency)

        assertEquals(expectedEntity, entity)
    }

    @Test
    fun `test updateEntityByRemote`() {
        val existingEntity = Currency(
            currencyCode = "USD",
            currencyName = "United States Dollar",
            numberCode = "840",
            precision = 2,
            symbol = "$",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
            isFavorite = true
        )

        val remoteCurrency =CurrencyRemoteModel(
            currencyCode = "USD",
            name = "US Dollar - updated",
            numberCode = "840",
            precision = 3,
            symbol = "$",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
        )

        val expectedUpdatedEntity = Currency(
            currencyCode = "USD",
            currencyName = "US Dollar - updated",
            numberCode = "840",
            precision = 3,
            symbol = "$",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
            isFavorite = true
        )

        val updatedEntity = currencyConverter.updateEntityByRemote(existingEntity, remoteCurrency)

        assertEquals(expectedUpdatedEntity, updatedEntity)
    }
}