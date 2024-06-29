package com.ofilip.exchange_rates.data.remote.retrofit.dataStore

import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.ofilip.exchange_rates.core.error.DataError
import com.ofilip.exchange_rates.data.remote.retrofit.ResponseWrapper
import java.io.IOException
import java.net.SocketTimeoutException
import retrofit2.Response
import timber.log.Timber

interface BaseRetrofitDataStore {
    private fun convertError(error: Exception): DataError {
        Timber.e(error, "Converting retrofit error")
        return when (error) {
            is DataError -> error
            is ValueInstantiationException -> DataError.InvalidOutputFormat(
                error.message
            )
            is SocketTimeoutException -> DataError.Timeout
            is IOException -> DataError.NoInternetConnection
            else -> DataError.Unknown(error)
        }
    }

    private fun <T> convertResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body() ?: throw DataError.InvalidOutputFormat()
        } else {
            Timber.e(
                "Converting retrofit response error: ${response.code()} ${
                    response.errorBody()?.string()
                }"
            )
            if (response.code() == 404) {
                throw DataError.ResourceNotFound
            } else if (response.code() == 401) {
                throw DataError.Unauthorized
            } else if (response.code() == 503) {
                throw DataError.ServiceUnavailable
            } else if (response.code() == 429) {
                throw DataError.RequestLimitReached
            }
            throw DataError.Unknown(data = response.errorBody()?.string())
        }
    }

    suspend fun <T> retrofitFetch(call: suspend () -> Response<T>): T {
        return try {
            convertResponse(call())
        } catch (e: Exception) {
            throw convertError(e)
        }
    }

    suspend fun <T> retrofitFetchWrappedResponse(call: suspend () -> Response<ResponseWrapper<T>>): T {
        return try {
            convertResponse(call()).response
        } catch (e: Exception) {
            throw convertError(e)
        }
    }

}
