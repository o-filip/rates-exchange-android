package com.ofilip.exchange_rates.ui.util

import android.content.Context
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.core.error.DataError
import com.ofilip.exchange_rates.core.error.DomainError
import com.ofilip.exchange_rates.core.error.UiError
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import timber.log.Timber

interface UiErrorConverter {
    fun convertToText(
        error: Throwable,
        overrideErrors: ((error: Throwable) -> Int?)? = null
    ): String
}

class UiErrorConverterImpl @Inject constructor(
    @ApplicationContext val context: Context
) : UiErrorConverter {

    override fun convertToText(
        error: Throwable,
        overrideErrors: ((error: Throwable) -> Int?)?
    ): String {
        Timber.e(error, "Converting an error to text")
        return overrideErrors?.invoke(error)
            ?.let { return context.getString(it) }
            ?: when (error) {
                is DataError -> convertDataError(error)
                is DomainError -> convertDomainError(error)
                is UiError -> convertUiError(error)
                else -> error.message ?: context.getString(R.string.error_unknown)
            }
    }


    private fun convertDataError(error: DataError): String =
        when (error) {
            is DataError.Unknown -> context.getString(R.string.error_data_unknown)
            is DataError.InvalidOutputFormat -> context.getString(R.string.error_data_invalid_output_format)
            DataError.NoInternetConnection -> context.getString(R.string.error_data_no_internet_connection)
            DataError.RequestLimitReached -> context.getString(R.string.error_data_request_limit_reached)
            DataError.ResourceNotFound -> context.getString(R.string.error_data_resource_not_found)
            DataError.ServiceUnavailable -> context.getString(R.string.error_data_service_unavailable)
            DataError.Timeout -> context.getString(R.string.error_data_timeout)
            DataError.Unauthorized -> context.getString(R.string.error_data_unauthorized)
        }


    private fun convertDomainError(error: DomainError): String =
        when (error) {
            is DomainError.CurrencyNotFound -> context.getString(R.string.error_domain_currency_not_found)
            is DomainError.CurrencyRateNotFound -> context.getString(R.string.error_domain_currency_rate_not_found)

        }

    private fun convertUiError(error: UiError): String =
        when (error) {
            UiError.CurrencyNotSelected -> context.getString(R.string.error_ui_currency_not_selected)
        }
}
