package com.ofilip.exchange_rates.ui.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Format currency rate in decimal format
 */
fun Double.formatCurrencyRate(): String = DecimalFormat("0.####").format(this)

/**
 * Formats unix timestamp in ms into full date time string
 */
fun Long.formatDateTime(): String =
    Date(this).let { date ->
        SimpleDateFormat("dd. MM. yyyy HH:mm", Locale.getDefault()).format(date)
    }
