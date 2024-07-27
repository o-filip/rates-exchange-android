package com.ofilip.exchange_rates.data.remote.ktor.utils

import io.ktor.client.plugins.logging.Logger
import timber.log.Timber

class KtorLogger : Logger {
    companion object {
        const val TAG = "KtorLogger"
    }

    override fun log(message: String) {
        Timber.tag(TAG).d(message)
    }
}
