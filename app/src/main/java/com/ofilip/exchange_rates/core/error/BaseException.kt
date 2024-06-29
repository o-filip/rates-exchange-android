package com.ofilip.exchange_rates.core.error

sealed class BaseException @JvmOverloads constructor(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception()

