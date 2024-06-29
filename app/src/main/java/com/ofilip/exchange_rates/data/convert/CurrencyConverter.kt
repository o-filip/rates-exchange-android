package com.ofilip.exchange_rates.data.convert

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import javax.inject.Inject

interface CurrencyConverter {
    fun remoteToEntity(
        remote: CurrencyRemoteModel
    ): Currency

    fun updateEntityByRemote(
        entity: Currency,
        remote: CurrencyRemoteModel
    ): Currency
}

class CurrencyConverterImpl @Inject constructor() : CurrencyConverter {
    override fun remoteToEntity(remote: CurrencyRemoteModel): Currency = Currency(
        currencyCode = remote.currencyCode,
        currencyName = remote.name,
        numberCode = remote.numberCode,
        precision = remote.precision,
        symbol = remote.symbol,
        symbolFirst = remote.symbolFirst,
        thousandsSeparator = remote.thousandsSeparator,
        decimalSeparator = remote.decimalSeparator,
        isFavorite = false

    )

    override fun updateEntityByRemote(
        entity: Currency,
        remote: CurrencyRemoteModel
    ): Currency =
        entity.copy(
            currencyName = remote.name,
            numberCode = remote.numberCode,
            precision = remote.precision,
            symbol = remote.symbol,
            symbolFirst = remote.symbolFirst,
            thousandsSeparator = remote.thousandsSeparator,
            decimalSeparator = remote.decimalSeparator,
        )
}
