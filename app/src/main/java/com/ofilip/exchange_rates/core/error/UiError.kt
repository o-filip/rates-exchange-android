package com.ofilip.exchange_rates.core.error

sealed class UiError @JvmOverloads constructor(
    override val message: String? = null,
    override val cause: Throwable? = null
) : BaseError() {
    data object CurrencyNotSelected : UiError() {
        private fun readResolve(): Any = CurrencyNotSelected
    }
}