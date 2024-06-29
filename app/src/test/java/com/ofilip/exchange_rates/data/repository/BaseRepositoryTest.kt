package com.ofilip.exchange_rates.data.repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BaseRepositoryTest {

    private val repository: BaseRepository = object : BaseRepository() {}

    @Test
    fun testFetchCachedResource_LocalData() = runBlocking {
        // Given
        val shouldFetchFromRemote: suspend () -> Boolean = { false }
        val fetchFromLocal: suspend () -> Flow<String> = { flowOf("Local Data") }

        // When
        val result = repository.fetchCachedResource(
            shouldFetchFromRemote = shouldFetchFromRemote,
            fetchFromLocal = fetchFromLocal,
            fetchFromRemote = { throw NotImplementedError() },
            store = { }
        )

        // Then
        result.collect { value ->
            assertEquals("Local Data", value)
        }
    }

    @Test
    fun testFetchCachedResource_RemoteData() = runBlocking {
        // Given
        val shouldFetchFromRemote: suspend () -> Boolean = { true }
        val fetchFromRemote: suspend () -> String = { "Remote Data" }
        val store: suspend (String) -> Unit = { /* Store implementation */ }
        val fetchFromLocal: suspend () -> Flow<String> = { flowOf("Local Data") }

        // When
        val result = repository.fetchCachedResource(
            shouldFetchFromRemote = shouldFetchFromRemote,
            fetchFromRemote = fetchFromRemote,
            store = store,
            fetchFromLocal = fetchFromLocal
        )

        // Then
        result.collect { value ->
            assertEquals("Local Data", value)
        }
    }
}
