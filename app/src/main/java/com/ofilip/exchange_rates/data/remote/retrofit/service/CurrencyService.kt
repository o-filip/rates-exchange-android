package com.ofilip.exchange_rates.data.remote.retrofit.service



import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.remote.retrofit.ResponseWrapper
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
}
