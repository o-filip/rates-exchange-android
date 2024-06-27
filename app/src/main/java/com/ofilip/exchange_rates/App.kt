package com.ofilip.exchange_rates

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ofilip.exchange_rates.domain.worker.CurrencyListUpdateWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        initWorkers()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun initWorkers() {
        val worker = PeriodicWorkRequestBuilder<CurrencyListUpdateWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            // Delay because, on first start, currencies are already automatically loaded
            .setInitialDelay(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                CURRENCY_LIST_UPDATE_WORK_ID,
                ExistingPeriodicWorkPolicy.KEEP,
                worker
            )
    }

    companion object {
        private const val CURRENCY_LIST_UPDATE_WORK_ID = "CurrencyListUpdateId"
    }
}
