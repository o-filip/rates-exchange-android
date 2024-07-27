package com.ofilip.exchange_rates.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRemoteModel(
    @SerialName("short_code")
    val currencyCode: String,
    @SerialName("name")
    val name: String?,
    @SerialName("code")
    val numberCode: String?,
    @SerialName("precision")
    val precision: Int?,
    @SerialName("symbol")
    val symbol: String?,
    @SerialName("symbol_first")
    val symbolFirst: Boolean?,
    @SerialName("thousands_separator")
    val thousandsSeparator: String?,
    @SerialName("decimal_mark")
    val decimalSeparator: String?,
)
