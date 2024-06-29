package com.ofilip.exchange_rates.ui.component.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ofilip.exchange_rates.R

@Composable
fun SpacerVertDimen(
    modifier: Modifier = Modifier,
    dimenId: Int
) {
    Spacer(modifier = modifier.height(dimensionResource(id = dimenId)))
}

@Composable
fun SpacerVertSmall(modifier: Modifier = Modifier) {
    SpacerVertDimen(
        modifier = modifier,
        dimenId = R.dimen.spacing_small
    )
}

@Composable
fun SpacerVertMedium(modifier: Modifier = Modifier) {
    SpacerVertDimen(
        modifier = modifier,
        dimenId = R.dimen.spacing_medium
    )
}

@Composable
fun SpacerVertLarge(modifier: Modifier = Modifier) {
    SpacerVertDimen(
        modifier = modifier,
        dimenId = R.dimen.spacing_large
    )
}

@Composable
fun SpacerHorizontalMedium(modifier: Modifier = Modifier) {
    SpacerVertDimen(
        modifier = modifier,
        dimenId = R.dimen.spacing_medium
    )
}
