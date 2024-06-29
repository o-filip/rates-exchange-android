package com.ofilip.exchange_rates.data.local.room.dataStore

import com.ofilip.exchange_rates.core.error.DataError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

interface BaseRoomDataStore {
    fun convertError(throwable: Throwable): Exception =
        if (throwable is CancellationException) {
            throwable
        } else {
            Timber.e(throwable, "Error while accessing Room database")
            DataError.LocalDataError(cause = throwable)
        }

    fun <T> roomFetch(
        call: () -> T
    ): T = try {
        call()
    } catch (ex: Exception) {
        throw convertError(ex)
    }

    suspend fun roomDo(
        call: suspend () -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            call()
        } catch (ex: Exception) {
            throw convertError(ex)
        }
    }

}
