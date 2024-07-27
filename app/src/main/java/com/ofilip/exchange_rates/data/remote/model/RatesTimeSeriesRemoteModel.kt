package com.ofilip.exchange_rates.data.remote.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.joda.time.DateTime

@Serializable
data class RatesTimeSeriesRemoteModel(
    @SerialName("response")
    val response: Map<@Contextual DateTime, Map<String, Double?>>
)
