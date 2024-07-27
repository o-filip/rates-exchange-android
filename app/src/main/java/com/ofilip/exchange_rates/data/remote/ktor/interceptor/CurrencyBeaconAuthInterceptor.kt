package com.ofilip.exchange_rates.data.remote.ktor.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Http interceptor adding api_key query parameter into every request
 */
class CurrencyBeaconAuthInterceptor(
    private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val newRequest = request
            .newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}
