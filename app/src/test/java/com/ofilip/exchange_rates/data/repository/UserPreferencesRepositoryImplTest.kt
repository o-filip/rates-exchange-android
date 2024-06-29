package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.data.local.dataStore.UserPreferencesLocalDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class UserPreferencesRepositoryImplTest {
    private val appProtoStore: UserPreferencesLocalDataStore = mock()
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @BeforeEach
    fun setup() {
        userPreferencesRepository = UserPreferencesRepositoryImpl(appProtoStore)
    }

    @Test
    fun `getDarkTheme should return the value from appProtoStore`() = runBlocking {
        val darkThemeValue = true

        appProtoStore.stub {
            onBlocking { darkTheme } doReturn flowOf(darkThemeValue)
        }

        val result = userPreferencesRepository.isDarkTheme.first()

        verify(appProtoStore).darkTheme
        assertEquals(darkThemeValue, result)
    }

    @Test
    fun `setDarkTheme should call appProtoStore setDarkTheme`() = runBlocking {
        val darkThemeValue = true

        userPreferencesRepository.setIsDarkTheme(darkThemeValue)

        verify(appProtoStore).setDarkTheme(darkThemeValue)
    }
}
