package com.ofilip.exchange_rates.data.remote.ktor.model

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

sealed class KtorResponse<T> {
    data class Success<T>(
        val response: HttpResponse,
        val data: T
    ) : KtorResponse<T>()

    class Error<T>(
        val response: HttpResponse,
        val exception: Exception? = null
    ) : KtorResponse<T>()
}

suspend inline fun <reified T> HttpResponse.toResponse(): KtorResponse<T> {
    return try {
        KtorResponse.Success(this, this.body<T>())
    } catch (e: Exception) {
        KtorResponse.Error(this, e)
    }
}
