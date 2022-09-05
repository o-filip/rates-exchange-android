package com.ofilip.exchange_rates.data.local.protoStore

import com.ofilip.exchange_rates.core.error.DataError
import timber.log.Timber

interface BaseProtoDataStore {
    fun convertError(error: Exception): DataError {
        Timber.e(error, "Error while accessing Proto DataStore")
        return DataError.Unknown(cause = error)
    }

    fun <T> protoFetch(
        call: () -> T
    ): T = try {
            call()
        } catch (ex: Exception) {
            throw convertError(ex)
        }

    suspend fun  protoDo(
        call: suspend () -> Unit
    ) = try {
            call()
        } catch (ex: Exception) {
            throw convertError(ex)
        }
}