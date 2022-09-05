package com.ofilip.exchange_rates.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker prefetching currency list form webservice and storing it into database
 */
@HiltWorker
class CurrencyListUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val currencyRepository: CurrencyRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return currencyRepository.prefetchCurrencies().let { result ->
            if (result.isSuccess) Result.success()
            else Result.failure()
        }
    }
}
