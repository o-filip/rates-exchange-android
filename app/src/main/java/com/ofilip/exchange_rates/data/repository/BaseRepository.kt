package com.ofilip.exchange_rates.data.repository


import com.ofilip.exchange_rates.core.error.DataError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber


abstract class BaseRepository {

    private fun convertError(error: Throwable): Throwable =
        if (error !is DataError) {
            Timber.e(error, "Repository error")
            DataError.Unknown(cause = error)
        } else {
            error
        }


    /**
     * Fetches data from remote (usually web service), store them in local data store
     * (usually database) and returns [Flow] of locally sored data
     */
    fun <RemoteType, LocalType> fetchCachedResource(
        /**
         * If true, try to fetch data from remote, otherwise skip remote fetch and load data
         * directly from local data store
         */
        shouldFetchFromRemote: suspend () -> Boolean = { true },

        /**
         * Fetch data from remote
         */
        fetchFromRemote: suspend () -> RemoteType,

        /**
         * Store given remote response into local store
         */
        store: suspend (RemoteType) -> Unit,

        /**
         * Fetch data from local data store
         */
        fetchFromLocal: suspend () -> Flow<LocalType>,

        /**
         * Called after data was successfully fetched from remote and stored into local data store
         */
        onSuccessfulRemoteFetch: suspend () -> Unit = {},
    ): Flow<Result<LocalType>> = flow {
        if (!shouldFetchFromRemote()) {
            emitAll(fetchFromLocal().map { Result.success(it) })
        } else {
            val remoteResponse = fetchFromRemote()

            store(remoteResponse)
            onSuccessfulRemoteFetch()
            emitAll(fetchFromLocal().map { Result.success(it) })
        }
    }.catch {
        emit(Result.failure(convertError(it)))
    }

    fun <T> repoFetch(
        call: () -> T
    ): T = try {
        call()
    } catch (ex: Exception) {
        throw convertError(ex)
    }

    suspend fun <T> repoFetchSuspend(
        call: suspend () -> T
    ): Result<T> = try {
        Result.success(call())
    } catch (ex: Exception) {
        Result.failure(convertError(ex))
    }


    suspend fun <T> repoDo(
        call: suspend () -> T
    ): Result<T> = try {
        Result.success(call())
    } catch (ex: Exception) {
        Result.failure(convertError(ex))
    }

    suspend fun repoDoNoResult(
        call: suspend () -> Unit
    ): Result<Unit> = try {
        Result.success(call())
    } catch (ex: Exception) {
        Result.failure(convertError(ex))
    }

}
