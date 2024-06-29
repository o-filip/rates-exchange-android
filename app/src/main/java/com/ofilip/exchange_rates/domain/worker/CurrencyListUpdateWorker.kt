package com.ofilip.exchange_rates.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

/**
 * Worker prefetching currency list form webservice and storing it into database
 * We don't expect currency list to change often, so we prefetch it once a day
 */
@HiltWorker
class CurrencyListUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val currencyRepository: CurrencyRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val TAG = "CurrencyListUpdateWorker"
    }

    override suspend fun doWork(): Result {
        try {
            currencyRepository.prefetchCurrencies()
            return Result.success()
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            return Result.failure()
        }
    }
}
