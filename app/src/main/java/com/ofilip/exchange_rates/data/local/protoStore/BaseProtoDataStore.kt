package com.ofilip.exchange_rates.data.local.protoStore

import com.ofilip.exchange_rates.core.error.DataError
import java.io.IOException
import kotlinx.coroutines.CancellationException
import timber.log.Timber

interface BaseProtoDataStore {
    fun convertError(error: Exception): Exception =
        if (error is CancellationException) {
            error
        } else {
            Timber.e(error, "Error while accessing Proto DataStore")
            DataError.LocalDataError(cause = error)
        }

    fun <T> protoFetch(
        call: () -> T
    ): T = try {
        call()
    } catch (ex: IOException) {
        throw convertError(ex)
    }

    suspend fun protoDo(
        call: suspend () -> Unit
    ) = try {
        call()
    } catch (ex: Exception) {
        throw convertError(ex)
    }
}
