package com.ofilip.exchange_rates.ui.component.button

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ofilip.exchange_rates.R

@Composable
fun ArrowNavBack(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArrowNavBackPreview() {
    ArrowNavBack(onClick = {})
}
