package com.ofilip.exchange_rates.data.remote.retrofit.service



import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.remote.model.RatesTimeSeriesRemoteModel
import com.ofilip.exchange_rates.data.remote.retrofit.ResponseWrapper
import com.ofilip.exchange_rates.data.remote.retrofit.converter.DateQuery
import org.joda.time.DateTime
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Response<CurrencyRatesRemoteModel>

    @GET("currencies")
    suspend fun getAllCurrencies(): Response<ResponseWrapper<List<CurrencyRemoteModel>>>

    @GET("timeseries")
    suspend fun getTimeSeries(
        @Query("start_date") @DateQuery startDate: DateTime,
        @Query("end_date") @DateQuery endDate: DateTime,
        @Query("base") base: String,
        @Query("symbols") symbols: List<String>? = null
    ): Response<RatesTimeSeriesRemoteModel>
}
