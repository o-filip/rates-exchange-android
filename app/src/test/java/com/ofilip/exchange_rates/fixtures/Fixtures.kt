package com.ofilip.exchange_rates.fixtures

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel

object Fixtures {
    val currenciesRemoteModels = listOf(
        CurrencyRemoteModel(
            currencyCode = "USD",
            name = "US Dollar",
            numberCode = "840",
            precision = 2,
            symbol = "$",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
        ),
        CurrencyRemoteModel(
            currencyCode = "GBP",
            name = "British Pound",
            numberCode = "826",
            precision = 2,
            symbol = "£",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
        ),
        CurrencyRemoteModel(
            currencyCode = "JPY",
            name = "Japanese Yen",
            numberCode = "392",
            precision = 0,
            symbol = "¥",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
        ),
    )

    val currencies = listOf(
        Currency(
            "USD",
            currencyName = "US Dollar",
            numberCode = "840",
            precision = 2,
            symbol = "$",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
            isFavorite = false,
        ),
        Currency(
            "GBP",
            currencyName = "British Pound",
            numberCode = "826",
            precision = 2,
            symbol = "£",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
            isFavorite = false,
        ),
        Currency(
            "JPY",
            currencyName = "Japanese Yen",
            numberCode = "392",
            precision = 0,
            symbol = "¥",
            symbolFirst = true,
            thousandsSeparator = ",",
            decimalSeparator = ".",
            isFavorite = false,
        ),
    )

    val currencyCodes = currencies.map { it.currencyCode }

    val remoteRates = CurrencyRatesRemoteModel(
        rates = mapOf(
            "USD" to 1.3,
            "GBP" to 0.8,
            "JPY" to 125.5
        ),
    )

    val remoteRatesEntries = remoteRates.rates.entries.toList()

    val localRates = listOf(
        CurrencyRate("USD", 1.3),
        CurrencyRate("GBP", 0.8),
        CurrencyRate("JPY", 125.5)
    )
}