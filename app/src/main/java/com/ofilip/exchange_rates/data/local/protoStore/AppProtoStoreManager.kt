package com.ofilip.exchange_rates.data.local.protoStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.ofilip.exchange_rates.AppProtoStore

import com.ofilip.exchange_rates.core.di.BaseRatesCurrency
import com.ofilip.exchange_rates.core.di.DefaultConversionFromCurrency
import com.ofilip.exchange_rates.core.di.DefaultConversionToCurrency
import com.ofilip.exchange_rates.core.extensions.filterUnchanged
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore: DataStore<AppProtoStore> by dataStore(
    fileName = "app_proto_store.pb",
    serializer = AppProtoStoreSerializer
)

/**
 * Class providing access to Proto store containing [AppProtoStore]
 */
@Singleton
class AppProtoStoreManager @Inject constructor(
    @ApplicationContext
    context: Context,
    @BaseRatesCurrency
    private val baseRatesCurrency: String,
    @DefaultConversionFromCurrency
    private val defaultConversionFromCurrency: String,
    @DefaultConversionToCurrency
    private val defaultConversionToCurrency: String,
) {
    private val store = context.settingsDataStore

    private val appProtoStore: Flow<AppProtoStore> = store.data

    val lastRatesLoadTimestampMs: Flow<Long> =
        appProtoStore.map { it.lastRatesLoadTimestampMs }.filterUnchanged()

    val overviewBaseCurrency: Flow<String> = appProtoStore.map {
        if (it.hasOverviewBaseCurrency()) it.overviewBaseCurrency
        else baseRatesCurrency
    }.filterUnchanged()

    val conversionCurrencyFrom: Flow<String> = appProtoStore.map {
        if (it.hasConversionCurrencyFrom()) it.conversionCurrencyFrom
        else defaultConversionFromCurrency
    }.filterUnchanged()

    val conversionCurrencyTo: Flow<String> = appProtoStore.map {
        if (it.hasConversionCurrencyTo()) it.conversionCurrencyTo
        else defaultConversionToCurrency
    }.filterUnchanged()

    val darkTheme: Flow<Boolean?> = appProtoStore.map {
        if (it.hasDarkTheme()) it.darkTheme
        else null
    }.filterUnchanged()

    suspend fun setLastRatesLoadTimestampMs(timestamp: Long) {
        store.updateData {
            it.toBuilder()
                .setLastRatesLoadTimestampMs(timestamp)
                .build()
        }
    }

    suspend fun setOverviewBaseCurrency(currencyCode: String) {
        store.updateData {
            it.toBuilder()
                .setOverviewBaseCurrency(currencyCode)
                .build()
        }
    }

    suspend fun setConversionCurrencyFrom(currencyCode: String) {
        store.updateData {
            it.toBuilder()
                .setConversionCurrencyFrom(currencyCode)
                .build()
        }
    }

    suspend fun setConversionCurrencyTo(currencyCode: String) {
        store.updateData {
            it.toBuilder()
                .setConversionCurrencyTo(currencyCode)
                .build()
        }
    }

    suspend fun setDarkTheme(darkTheme: Boolean) {
        store.updateData {
            it.toBuilder()
                .setDarkTheme(darkTheme)
                .build()
        }
    }
}
