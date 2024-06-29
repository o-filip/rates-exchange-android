package com.ofilip.exchange_rates.data.remote.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class CurrencyRatesRemoteModel(
    @JsonProperty("rates")
    val rates: Map<String, Double?>
)
