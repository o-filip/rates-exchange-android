package com.ofilip.exchange_rates.core.error

sealed class DataError @JvmOverloads constructor(
    override val message: String? = null,
    override val cause: Throwable? = null
) : BaseError() {
    data object NoInternetConnection : DataError() {
        private fun readResolve(): Any = NoInternetConnection
    }

    data object Timeout: DataError() {
        private fun readResolve(): Any = Timeout
    }

    data class InvalidOutputFormat(
        val data: Any? = null
    ): DataError()

    data object ResourceNotFound : DataError() {
        private fun readResolve(): Any = ResourceNotFound
    }

    data object Unauthorized: DataError() {
        private fun readResolve(): Any = Unauthorized
    }

    data object ServiceUnavailable: DataError() {
        private fun readResolve(): Any = ServiceUnavailable
    }

    data object RequestLimitReached: DataError() {
        private fun readResolve(): Any = RequestLimitReached
    }

    data class Unknown @JvmOverloads constructor(
        override val cause: Throwable? = null,
        val data: Any? = null
    ) : DataError()
}