package com.ofilip.exchange_rates.core.error

sealed class DomainError @JvmOverloads constructor(
    override val message: String? = null,
    override val cause: Throwable? = null
) : BaseError() {
    data class CurrencyRateNotFound(val currencyCode: String) : DomainError()
    data class CurrencyNotFound(val currencyCode: String) : DomainError()
}
