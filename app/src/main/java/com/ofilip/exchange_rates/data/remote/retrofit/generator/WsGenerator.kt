package com.ofilip.exchange_rates.data.remote.retrofit.generator


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.ofilip.exchange_rates.BuildConfig
import com.ofilip.exchange_rates.data.remote.retrofit.converter.DateConverterFactory
import com.ofilip.exchange_rates.data.remote.retrofit.converter.DateDeserializer
import com.ofilip.exchange_rates.data.remote.retrofit.interceptor.ApiLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class WsGenerator(
    apiBaseUrl: String
) {

    private val builder = Retrofit.Builder()
        .baseUrl(apiBaseUrl)
        .addConverterFactory(DateConverterFactory())
        .addConverterFactory(getFactory())

    /**
     * Creates web service [S] using given [options]
     */
    fun <S> createService(serviceClass: Class<S>, options: WsGeneratorOptions? = null): S {
        val httpClient = prepareHttpClientBuilder(options)

        addLoggingInterceptor(httpClient)

        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(serviceClass)
    }

    private fun addLoggingInterceptor(builder: OkHttpClient.Builder) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor(ApiLogger()).also { interceptor ->
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(interceptor)
            }
        }
    }

    private fun getFactory(): JacksonConverterFactory {
        val objectMapper = ObjectMapper()
        val module = SimpleModule()

        module.addKeyDeserializer(DateTime::class.java, DateDeserializer())

        objectMapper.registerModule(module)

        return JacksonConverterFactory.create(objectMapper)
    }

    private fun prepareHttpClientBuilder(options: WsGeneratorOptions? = null): OkHttpClient.Builder =
        OkHttpClient.Builder().apply {
            options?.interceptors?.forEach {
                addInterceptor(it)
            }
        }
}
