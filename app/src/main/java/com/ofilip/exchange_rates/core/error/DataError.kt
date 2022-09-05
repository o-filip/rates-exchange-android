package com.ofilip.exchange_rates.core.error

sealed class DataError @JvmOverloads constructor(
    override val message: String? = null,
    override val cause: Throwable? = null
) : BaseError() {
    object NoInternetConnection : DataError()

    object Timeout: DataError()

    data class InvalidOutputFormat(
        val data: Any? = null
    ): DataError()

    object ResourceNotFound : DataError()

    object Unauthorized: DataError()

    object ServiceUnavailable: DataError()

    object RequestLimitReached: DataError()

    data class Unknown @JvmOverloads constructor(
        override val cause: Throwable? = null,
        val data: Any? = null
    ) : DataError()
}
