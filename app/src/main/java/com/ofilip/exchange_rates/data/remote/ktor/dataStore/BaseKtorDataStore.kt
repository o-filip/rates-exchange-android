package com.ofilip.exchange_rates.data.remote.ktor.dataStore

import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.ofilip.exchange_rates.core.error.DataError
import com.ofilip.exchange_rates.data.remote.ktor.model.KtorResponse
import com.ofilip.exchange_rates.data.remote.ktor.model.ResponseWrapper

import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

interface BaseKtorDataStore {
    private fun convertError(error: Exception): Exception {
        Timber.e(error, "Converting ktor error")
        return when (error) {
            is DataError, is CancellationException -> error
            is ValueInstantiationException -> DataError.InvalidOutputFormat(
                error.message
            )

            is SocketTimeoutException -> DataError.Timeout
            is IOException -> DataError.NoInternetConnection
            else -> DataError.Unknown(error)
        }
    }

    private fun <T> convertResponse(response: KtorResponse<T>): T {
        when (response) {
            is KtorResponse.Success -> {
                return response.data
            }

            is KtorResponse.Error -> {
                Timber.e(
                    "Converting ktor response error: ${response.response.status.value}"
                )
                when (response.response.status.value) {
                    404 -> throw DataError.ResourceNotFound
                    401 -> throw DataError.Unauthorized
                    503 -> throw DataError.ServiceUnavailable
                    429 -> throw DataError.RequestLimitReached
                    else -> throw DataError.Unknown(data = response.response)
                }
            }
        }
    }

    suspend fun <T> ktorFetch(call: suspend () -> KtorResponse<T>): T {
        return try {
            convertResponse(call())
        } catch (e: Exception) {
            throw convertError(e)
        }
    }

    suspend fun <T> ktorFetchWrappedResponse(call: suspend () -> KtorResponse<ResponseWrapper<T>>): T {
        return try {
            convertResponse(call()).response
        } catch (e: Exception) {
            throw convertError(e)
        }
    }

}
