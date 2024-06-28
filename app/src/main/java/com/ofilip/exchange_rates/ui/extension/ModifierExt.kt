package com.ofilip.exchange_rates.ui.extension

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.ofilip.exchange_rates.ui.util.Dimens

/**
 * Applies given [modifier] only if [condition] is true
 */
@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier = composed {
    if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}


fun Modifier.screenHorizontalPadding(): Modifier = composed {
    then(padding(horizontal = Dimens.screenHorizontalPadding()))
}


