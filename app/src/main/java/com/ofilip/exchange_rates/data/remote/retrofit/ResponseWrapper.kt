package com.ofilip.exchange_rates.data.remote.retrofit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseWrapper<T>(
    @JsonProperty("response") val response: T,
)
