package com.ofilip.exchange_rates.data.remote.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CurrencyRemoteModel(
    @JsonProperty("short_code")
    val currencyCode: String,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("code")
    val numberCode: String?,
    @JsonProperty("precision")
    val precision: Int?,
    @JsonProperty("symbol")
    val symbol: String?,
    @JsonProperty("symbol_first")
    val symbolFirst: Boolean?,
    @JsonProperty("thousands_separator")
    val thousandsSeparator: String?,
    @JsonProperty("decimal_mark")
    val decimalSeparator: String?,
)
