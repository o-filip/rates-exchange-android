package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.core.extensions.toResultFlow
import com.ofilip.exchange_rates.data.convert.CurrencyConverter
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyLocalDataStore
import com.ofilip.exchange_rates.data.remote.dataStore.CurrencyRemoteDataStore
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.util.RemoteAndLocalModelDiffer
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteDataStore: CurrencyRemoteDataStore,
    private val currencyLocalDataStore: CurrencyLocalDataStore,
    private val currencyConverter: CurrencyConverter,
) : BaseRepository(), CurrencyRepository {

    override suspend fun prefetchCurrencies(): Result<Unit> = repoDoNoResult {
        val remoteResponse = remoteDataStore.getAllCurrencies()
        // Locally stored currencies contains favorite flag, which needs to be
        // preserved. We use updateLocalCurrenciesByRemoteModels to update
        // local currencies with remote ones, but preserve favorite flag
        updateLocalCurrenciesByRemoteModels(remoteResponse)
    }

    override fun getCurrencies(
        textQuery: String,
        onlyFavorites: Boolean,
    ): Flow<Result<List<Currency>>> = repoFetch {
        currencyLocalDataStore.getAll(textQuery, onlyFavorites).map { Result.success(it) }
    }

    private suspend fun updateLocalCurrenciesByRemoteModels(
        remoteCurrencies: List<CurrencyRemoteModel>
    ) {
        RemoteAndLocalModelDiffer(
            remoteItems = remoteCurrencies,
            localItems = currencyLocalDataStore.getAll().first(),
            areItemsSame = { local, remote -> local.currencyCode == remote.currencyCode },
            updateLocalItem = { local, remote ->
                currencyConverter.updateEntityByRemote(local, remote)
            },
            createLocalItem = { remote -> currencyConverter.remoteToEntity(remote) },
        ).also { diffResult ->
            insertCurrencies(diffResult.itemsToUpdateOrInsert())
            deleteCurrencies(diffResult.itemsToDelete())
        }
    }

    override suspend fun updateCurrencyFavoriteState(
        currency: Currency,
        favoriteState: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        repoDoNoResult {
            currencyLocalDataStore.update(currency.copy(isFavorite = favoriteState))
        }
    }

    override fun getCurrency(currencyCode: String): Flow<Result<Currency>> = repoFetch {
        currencyLocalDataStore.getByCode(currencyCode).toResultFlow()
    }


    private suspend fun insertCurrencies(currencies: List<Currency>): Result<Unit> =
        repoDoNoResult {
            currencyLocalDataStore.insert(currencies)
        }

    private suspend fun deleteCurrencies(currencies: List<Currency>): Result<Unit> =
        repoDoNoResult {
            currencyLocalDataStore.delete(currencies)
        }

    override suspend fun areCurrenciesLoaded(): Result<Boolean> = repoDo {
        currencyLocalDataStore.getAll().first().isNotEmpty()
    }

    override val overviewBaseCurrency: Flow<Result<String>>
        get() = repoFetch { currencyLocalDataStore.overviewBaseCurrency.toResultFlow() }

    override val conversionCurrencyFrom: Flow<Result<String>>
        get() = repoFetch { currencyLocalDataStore.conversionCurrencyFrom.toResultFlow() }

    override val conversionCurrencyTo: Flow<Result<String>>
        get() = repoFetch { currencyLocalDataStore.conversionCurrencyTo.toResultFlow() }

    override suspend fun setOverviewBaseCurrency(currencyCode: String): Result<Unit> =
        repoDoNoResult {
            currencyLocalDataStore.setOverviewBaseCurrency(currencyCode)
        }

    override suspend fun setConversionCurrencyFrom(currencyCode: String): Result<Unit> =
        repoDoNoResult {
            currencyLocalDataStore.setConversionCurrencyFrom(currencyCode)
        }

    override suspend fun setConversionCurrencyTo(currencyCode: String): Result<Unit> =
        repoDoNoResult {
            currencyLocalDataStore.setConversionCurrencyTo(currencyCode)
        }
}