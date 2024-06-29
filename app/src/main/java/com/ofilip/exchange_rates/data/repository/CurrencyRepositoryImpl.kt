package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.convert.CurrencyConverter
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyLocalDataStore
import com.ofilip.exchange_rates.data.remote.dataStore.CurrencyRemoteDataStore
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.util.RemoteAndLocalModelDiffer
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteDataStore: CurrencyRemoteDataStore,
    private val currencyLocalDataStore: CurrencyLocalDataStore,
    private val currencyConverter: CurrencyConverter,
) : BaseRepository(), CurrencyRepository {

    override suspend fun prefetchCurrencies() = repoDoWithoutResult {
        val remoteResponse = remoteDataStore.getAllCurrencies()
        // Locally stored currencies contains favorite flag, which needs to be
        // preserved. We use updateLocalCurrenciesByRemoteModels to update
        // local currencies with remote ones, but preserve favorite flag
        updateLocalCurrenciesByRemoteModels(remoteResponse)
    }

    override fun getCurrencies(
        textQuery: String,
        onlyFavorites: Boolean,
    ): Flow<List<Currency>> = repoFetch {
        currencyLocalDataStore.getAll(textQuery, onlyFavorites)
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
            insertCurrencies(diffResult.itemsToUpdateOrInsert)
            deleteCurrencies(diffResult.itemsToDelete)
        }
    }

    override suspend fun updateCurrencyFavoriteState(
        currency: Currency,
        favoriteState: Boolean
    ) = repoDoWithoutResult {
        currencyLocalDataStore.update(currency.copy(isFavorite = favoriteState))
    }


    override fun getCurrency(currencyCode: String): Flow<Currency> = repoFetch {
        currencyLocalDataStore.getByCode(currencyCode)
    }

    private suspend fun insertCurrencies(currencies: List<Currency>) =
        repoDoWithoutResult {
            currencyLocalDataStore.insert(currencies)
        }

    private suspend fun deleteCurrencies(currencies: List<Currency>) =
        repoDoWithoutResult {
            currencyLocalDataStore.delete(currencies)
        }

    override suspend fun areCurrenciesLoaded(): Boolean = repoDo {
        currencyLocalDataStore.getAll().first().isNotEmpty()
    }

    override val overviewBaseCurrency: Flow<String>
        get() = repoFetch { currencyLocalDataStore.overviewBaseCurrency }

    override val conversionCurrencyFrom: Flow<String>
        get() = repoFetch { currencyLocalDataStore.conversionCurrencyFrom }

    override val conversionCurrencyTo: Flow<String>
        get() = repoFetch { currencyLocalDataStore.conversionCurrencyTo }

    override suspend fun setOverviewBaseCurrency(currencyCode: String) =
        repoDoWithoutResult {
            currencyLocalDataStore.setOverviewBaseCurrency(currencyCode)
        }

    override suspend fun setConversionCurrencyFrom(currencyCode: String) =
        repoDoWithoutResult {
            currencyLocalDataStore.setConversionCurrencyFrom(currencyCode)
        }

    override suspend fun setConversionCurrencyTo(currencyCode: String) =
        repoDoWithoutResult {
            currencyLocalDataStore.setConversionCurrencyTo(currencyCode)
        }
}
