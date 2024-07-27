package com.ofilip.exchange_rates.data.remote.ktor.generator

import com.ofilip.exchange_rates.data.remote.ktor.convertor.DateTimeSerializer
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.Interceptor
import org.joda.time.DateTime


class KtorWsGenerator {

    fun createClient(
        baseUrl: String,
        interceptors: List<Interceptor>,
        logger: Logger
    ): HttpClient = HttpClient(OkHttp) {
        defaultRequest {
            url(baseUrl)
            this.contentType(ContentType.Application.Json)
        }

        engine {
            config {
                for (interceptor in interceptors) {
                    addInterceptor(interceptor)
                }
            }
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                serializersModule = SerializersModule {
                    contextual(DateTime::class, DateTimeSerializer)
                }
            })
        }

        install(Logging) {
            level = LogLevel.ALL
            this.logger = logger
        }
    }
}
