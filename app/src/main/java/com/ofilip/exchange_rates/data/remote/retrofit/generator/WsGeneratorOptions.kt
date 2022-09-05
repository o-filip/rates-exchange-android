package com.ofilip.exchange_rates.data.remote.retrofit.generator

import okhttp3.Interceptor

/**
 * Class holding configuration options for creation of a new web service
 */
class WsGeneratorOptions(
    val interceptors: List<Interceptor> = emptyList()
)
