package com.ofilip.exchange_rates.ui.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.joda.time.DateTime
import org.joda.time.Duration

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

/**
 * Formats unix timestamp in ms into short date string (Month and day)
 */
fun DateTime.formatShortDate(): String =
    toString(SimpleDateFormat("MMM dd", Locale.getDefault()).toPattern())

/**
 * Formats unix timestamp in ms into short date string (Month and day) with day offset
 */
fun DateTime.formatShotDateWithOffset(dayOffset: Int): String =
    plus(Duration.standardDays(dayOffset.toLong())).formatShortDate()
