package com.ofilip.exchange_rates.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.component.button.ArrowNavBack

@Composable
fun SimpleTopBar(
    title: @Composable () -> Unit,
    onNavigateBack: (() -> Unit)? = null
) {
    TopAppBar(
        elevation = 0.dp,
        title =  title,
        navigationIcon = if (onNavigateBack == null) {
            null
        } else {
            { ArrowNavBack(onClick = onNavigateBack) }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}
