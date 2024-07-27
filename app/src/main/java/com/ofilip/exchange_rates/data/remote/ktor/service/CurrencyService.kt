package com.ofilip.exchange_rates.data.remote.ktor.service

import com.ofilip.exchange_rates.data.remote.ktor.utils.dateParameter
import com.ofilip.exchange_rates.data.remote.ktor.model.KtorResponse
import com.ofilip.exchange_rates.data.remote.ktor.model.ResponseWrapper
import com.ofilip.exchange_rates.data.remote.ktor.model.toResponse
import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.remote.model.RatesTimeSeriesRemoteModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.joda.time.DateTime

class CurrencyService(
    private val client: HttpClient
) {
    suspend fun getLatestRates(
        base: String,
        symbols: String
    ): KtorResponse<CurrencyRatesRemoteModel> = client.get("latest") {
        parameter("base", base)
        parameter("symbols", symbols)
    }.toResponse()

    suspend fun getAllCurrencies(): KtorResponse<ResponseWrapper<List<CurrencyRemoteModel>>> =
        client.get("currencies").toResponse()

    suspend fun getTimeSeries(
        startDate: DateTime,
        endDate: DateTime,
        base: String,
        symbols: String? = null
    ): KtorResponse<RatesTimeSeriesRemoteModel> = client.get("timeseries") {
        dateParameter("start_date", startDate)
        dateParameter("end_date", endDate)
        parameter("base", base)
        if (symbols != null) {
            parameter("symbols", symbols)
        }
    }.toResponse()
}
