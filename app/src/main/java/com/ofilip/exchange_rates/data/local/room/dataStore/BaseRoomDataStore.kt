package com.ofilip.exchange_rates.data.local.room.dataStore

import com.ofilip.exchange_rates.core.error.DataError
import timber.log.Timber

interface BaseRoomDataStore {
    fun convertError(throwable: Throwable): DataError {
        Timber.e(throwable, "Error while accessing Room database")
        return DataError.Unknown(cause = throwable)
    }

    fun <T> roomFetch(
        call: () -> T
    ): T = try {
            call()
        } catch (ex: Exception) {
            throw convertError(ex)
        }

    suspend fun  roomDo(
        call: suspend () -> Unit
    ) = try {
            call()
        } catch (ex: Exception) {
            throw convertError(ex)
        }

}