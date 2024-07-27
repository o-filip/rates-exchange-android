package com.ofilip.exchange_rates.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CurrencyRatesRemoteModel(
    @SerialName("rates")
    val rates: Map<String, Double?>
)
