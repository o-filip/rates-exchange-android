package com.ofilip.exchange_rates.data.remote.ktor.utils

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import org.joda.time.DateTime

fun HttpRequestBuilder.dateParameter(name: String, dateTime: DateTime) {
    parameter(name, dateTime.toString("yyyy-MM-dd"))
}
