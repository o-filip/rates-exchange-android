package com.ofilip.exchange_rates.data.remote.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class RatesTimeSeriesRemoteModel(
    @JsonProperty("response")
    val response: Map<DateTime, Map<String, Double?>>
)
