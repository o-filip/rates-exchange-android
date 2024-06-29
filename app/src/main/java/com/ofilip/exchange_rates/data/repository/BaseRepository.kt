package com.ofilip.exchange_rates.data.repository


import com.ofilip.exchange_rates.core.error.DataError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import timber.log.Timber


abstract class BaseRepository {

    private fun convertError(error: Throwable): Throwable =
        if (error is CancellationException || error is DataError) {
            error
        } else {
            Timber.e(error, "Repository error")
            DataError.Unknown(cause = error)
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
    ): Flow<LocalType> = flow {
        if (!shouldFetchFromRemote()) {
            emitAll(fetchFromLocal())
        } else {
            val remoteResponse = fetchFromRemote()

            store(remoteResponse)
            onSuccessfulRemoteFetch()
            emitAll(fetchFromLocal())
        }
    }.catch {
        throw convertError(it)
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
    ): T = try {
        call()
    } catch (ex: Exception) {
        throw convertError(ex)
    }


    suspend fun <T> repoDo(
        call: suspend () -> T
    ): T = try {
        call()
    } catch (ex: Exception) {
        throw convertError(ex)
    }

    suspend fun repoDoWithoutResult(
        call: suspend () -> Unit
    ): Unit = try {
        call()
    } catch (ex: Exception) {
        throw convertError(ex)
    }

}
