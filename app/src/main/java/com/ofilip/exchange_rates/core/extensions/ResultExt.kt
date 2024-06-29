package com.ofilip.exchange_rates.core.extensions


fun <T> Result<T>.onEither(action: (Result<T>) -> Unit): Result<T> {
    action(this)
    return this
}
