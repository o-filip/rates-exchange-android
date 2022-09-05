package com.ofilip.exchange_rates.core.error

sealed class UiError @JvmOverloads constructor(
    override val message: String? = null,
    override val cause: Throwable? = null
) : BaseError() {
    object CurrencyNotSelected : UiError()
}